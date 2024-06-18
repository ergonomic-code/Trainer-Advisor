package pro.qyoga.core.therapy.exercises.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.spring.sdj.ergo.ErgoRepository
import pro.azhidkov.platform.spring.sdj.sortBy
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.exercises.model.Exercise


@Repository
class ExercisesRepo(
    jdbcAggregateTemplate: JdbcAggregateOperations,
    namedParameterJdbcOperations: NamedParameterJdbcOperations,
    jdbcConverter: JdbcConverter,
    relationalMappingContext: RelationalMappingContext,
) : ErgoRepository<Exercise, Long>(
    jdbcAggregateTemplate,
    namedParameterJdbcOperations,
    Exercise::class,
    jdbcConverter,
    relationalMappingContext
) {

    fun findExerciseSummaries(exercisesSearchDto: ExerciseSearchDto, page: Pageable): Page<ExerciseSummaryDto> {
        val entitiesPage = findPage(PageRequest.of(page.pageNumber, page.pageSize, sortBy(Exercise::title))) {
            Exercise::title containsIfNotNull exercisesSearchDto.title
            Exercise::exerciseType isEqualIfNotNull exercisesSearchDto.exerciseType
        }
        return entitiesPage.map {
            it.toSummaryDto()
        }
    }

}

