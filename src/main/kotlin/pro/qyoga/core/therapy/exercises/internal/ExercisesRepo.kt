package pro.qyoga.core.therapy.exercises.internal

import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.util.TypeInformation
import org.springframework.stereotype.Repository
import pro.qyoga.core.therapy.exercises.api.dtos.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.api.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.exercises.api.model.Exercise
import pro.qyoga.platform.spring.sdj.erpo.ErgoRepository
import pro.qyoga.platform.spring.sdj.example
import pro.qyoga.platform.spring.sdj.probeFrom
import pro.qyoga.platform.spring.sdj.sortBy


@Repository
class ExercisesRepo(
    jdbcAggregateTemplate: JdbcAggregateOperations,
    jdbcConverter: JdbcConverter
) : ErgoRepository<Exercise, Long>(
    jdbcAggregateTemplate,
    BasicPersistentEntity(TypeInformation.of(Exercise::class.java)),
    jdbcConverter
) {

    fun findExerciseSummaries(exercisesSearchDto: ExerciseSearchDto, page: Pageable): Page<ExerciseSummaryDto> {
        val example = example<Exercise>(probeFrom(exercisesSearchDto)) {
            withMatcher(exercisesSearchDto::title, GenericPropertyMatcher().contains().ignoreCase())
            withMatcher(exercisesSearchDto::exerciseType, GenericPropertyMatcher().contains().ignoreCase())
        }

        return findAll(example, PageRequest.of(page.pageNumber, page.pageSize, sortBy(Exercise::title))).map {
            it.toSummaryDto()
        }
    }

}