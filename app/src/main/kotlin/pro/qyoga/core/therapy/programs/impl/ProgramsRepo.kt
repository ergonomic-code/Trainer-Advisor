package pro.qyoga.core.therapy.programs.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.intellij.lang.annotations.Language
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.data.util.TypeInformation
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.spring.sdj.erpo.ErgoRepository
import pro.azhidkov.platform.spring.sdj.sortBy
import pro.qyoga.core.therapy.programs.dtos.ProgramsSearchFilter
import pro.qyoga.core.therapy.programs.model.DocxProgram
import pro.qyoga.core.therapy.programs.model.Program
import kotlin.reflect.KProperty1

@Repository
class ProgramsRepo(
    val jdbcAggregateTemplate: JdbcAggregateOperations,
    val namedParameterJdbcOperations: NamedParameterJdbcOperations,
    val objectMapper: ObjectMapper,
    relationalMappingContext: RelationalMappingContext,
    jdbcConverter: JdbcConverter
) : ErgoRepository<Program, Long>(
    jdbcAggregateTemplate,
    namedParameterJdbcOperations,
    BasicPersistentEntity(TypeInformation.of(Program::class.java)),
    jdbcConverter,
    relationalMappingContext
) {

    object Page {
        val firstTenByTitle = PageRequest.of(0, 10, sortBy(Program::title))
    }

}

fun ProgramsRepo.findAllMatching(
    programsSearchFilter: ProgramsSearchFilter,
    pageRequest: PageRequest,
    fetch: List<KProperty1<Program, *>> = emptyList()
): Page<Program> {
    @Language("PostgreSQL")
    val query = """
        SELECT p.* FROM programs p
        JOIN therapeutic_tasks tt on p.therapeutic_task_ref = tt.id
        WHERE 
            tt.name ilike '%' || :therapeuticTaskKeyword || '%' AND
            p.title ilike '%' || :titleKeyword || '%'
    """.trimIndent()

    val params = mapOf(
        "titleKeyword" to programsSearchFilter.titleKeyword,
        "therapeuticTaskKeyword" to programsSearchFilter.therapeuticTaskKeyword
    )

    return findPage(query, params, pageRequest, fetch)
}

fun ProgramsRepo.findDocxOrNull(id: Long): DocxProgram? {
    @Language("PostgreSQL")
    val query = """
        with
        exercise_steps as (
            select es.step_index, es.description, es.image_id, es.exercise_id from exercise_steps es
        ),
        exercises AS (
            select e.id, pe.exercise_index, e.title, e.description, pe.program_id, json_agg(es order by step_index) AS steps
            from program_exercises pe
                left join exercises e on e.id = pe.exercise_ref
                join exercise_steps es on e.id = es.exercise_id
            group by e.id, pe.exercise_index, e.title, e.description, pe.program_id
        ),
        programs as (
            select p.id, p.title, json_agg(e order by exercise_index) AS exercises
            from programs p left
                join exercises e on p.id = e.program_id
            group by p.id, p.title
        )
        SELECT json_agg(p.*)
        FROM programs p
        where p.id = :id
    """.trimIndent()

    val typeRef: TypeReference<List<DocxProgram>> = object : TypeReference<List<DocxProgram>>() {}
    val result = namedParameterJdbcOperations.queryForObject(query, mapOf("id" to id)) { rs, _ ->
        val json = rs.getString(1)
        if (json != null) objectMapper.readValue(json, typeRef) else emptyList()
    }
    return result?.firstOrNull()
}
