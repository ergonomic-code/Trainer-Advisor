package pro.qyoga.core.therapy.programs

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
import pro.qyoga.core.therapy.programs.dtos.ProgramsSearchFilter
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.platform.spring.sdj.erpo.ErgoRepository
import pro.qyoga.platform.spring.sdj.sortBy
import kotlin.reflect.KProperty1

@Repository
class ProgramsRepo(
    val jdbcAggregateTemplate: JdbcAggregateOperations,
    namedParameterJdbcOperations: NamedParameterJdbcOperations,
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
