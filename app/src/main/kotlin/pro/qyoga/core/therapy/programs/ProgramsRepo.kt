package pro.qyoga.core.therapy.programs

import com.fasterxml.jackson.databind.ObjectMapper
import org.intellij.lang.annotations.Language
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.spring.jdbc.rowMapperFor
import pro.azhidkov.platform.spring.sdj.ergo.ErgoRepository
import pro.azhidkov.platform.spring.sdj.sortBy
import pro.qyoga.core.therapy.programs.dtos.ProgramsSearchFilter
import pro.qyoga.core.therapy.programs.model.DocxProgram
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.core.therapy.programs.views.ProgramSummaryView
import kotlin.reflect.KProperty1

@Repository
class ProgramsRepo(
    jdbcAggregateTemplate: JdbcAggregateOperations,
    namedParameterJdbcOperations: NamedParameterJdbcOperations,
    relationalMappingContext: RelationalMappingContext,
    jdbcConverter: JdbcConverter,
    objectMapper: ObjectMapper
) : ErgoRepository<Program, Long>(
    jdbcAggregateTemplate,
    namedParameterJdbcOperations,
    Program::class,
    jdbcConverter,
    relationalMappingContext
) {

    object Page {
        val firstTenByTitle = PageRequest.of(0, 10, sortBy(Program::title))
    }

    internal val docxProgramRowMapper = rowMapperFor<DocxProgram>(objectMapper)

    internal val programSummaryViewRowMapper = rowMapperFor<ProgramSummaryView>(objectMapper)

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

fun ProgramsRepo.findDocxById(programId: Long): DocxProgram? {
    @Language("PostgreSQL")
    val query = """
        with
        exercises AS (
            select e.id, pe.exercise_index, e.title, e.description, pe.program_id, json_agg(es order by step_index) AS steps
            from program_exercises pe
                left join exercises e on e.id = pe.exercise_ref
                join exercise_steps es on e.id = es.exercise_id
            group by e.id, pe.program_id, pe.exercise_index
        ),
        programs as (
            select p.id, p.title, json_agg(e order by exercise_index) AS exercises
            from programs p left
                join exercises e on p.id = e.program_id
            group by p.id
        )
        SELECT to_json(p.*)
        FROM programs p
        where p.id = :id
    """.trimIndent()

    return findOne(query, mapOf("id" to programId), docxProgramRowMapper)
}

fun ProgramsRepo.getSummaryById(programId: Long): ProgramSummaryView? {
    @Language("PostgreSQL")
    val query = """
        WITH
        exercises AS (
            SELECT e.id, pe.exercise_index, e.title, e.description, format_to_iso_8601(e.duration) duration, e.exercise_type type, pe.program_id
            FROM program_exercises pe
                LEFT JOIN exercises e on e.id = pe.exercise_ref
        ),
        programs AS (
            SELECT p.id, p.title, json_build_object('id', tt.id, 'name', tt.name) "therapeuticTask", json_agg(e order by exercise_index) AS exercises
            FROM programs p left
                JOIN exercises e on p.id = e.program_id
                JOIN therapeutic_tasks tt on p.therapeutic_task_ref = tt.id
            GROUP BY p.id, tt.id
        )
        SELECT to_json(p.*)
        FROM programs p
        WHERE p.id = :id
    """.trimIndent()

    return findOne(query, mapOf("id" to programId), programSummaryViewRowMapper)
}