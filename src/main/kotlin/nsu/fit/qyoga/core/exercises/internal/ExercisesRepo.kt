package nsu.fit.qyoga.core.exercises.internal

import nsu.fit.platform.lang.jdbc.get
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import org.intellij.lang.annotations.Language
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet

@Language("PostgreSQL")
const val SELECT_EXERCISES_BY_FILTERS_QUERY = """
        SELECT ex.id as id, title, description, indications, contradictions, duration, et.id type_id, et.name as type_title, array_agg(tp.purpose) as purposes
        FROM exercises ex
            INNER JOIN exercise_purposes ep ON ex.id = ep.exercise_id
            INNER JOIN therapeutic_purposes tp ON tp.id = ep.purpose_id
            INNER JOIN exercise_types et on ex.exercise_type_id = et.id
        WHERE (ex.title LIKE '%' || :#{#search.title ?: ""} || '%')
            AND (ex.contradictions LIKE '%' || :#{#search?.contradiction ?: ""} || '%')
            AND (et.id = :#{#search.exerciseTypeId ?: 0} OR :#{#search.exerciseTypeId ?: 0} = 0)
            AND (tp.purpose LIKE '%' || :#{#search.therapeuticPurpose ?: ""} || '%')
        GROUP BY ex.id, et.id
"""

@JvmDefaultWithoutCompatibility
@Repository
@Transactional(readOnly = false)
interface ExercisesRepo : CrudRepository<Exercise, Long>, PagingAndSortingRepository<Exercise, Long> {

    fun getExercisesByFilters(search: ExerciseSearchDto, pageRequest: Pageable): Page<ExerciseDto> {
        val count = countExercises(search)
        if (count == 0L) {
            return Page.empty()
        }
        val page = getExercisesByFilters(search, pageRequest.offset, pageRequest.pageSize)
        return PageImpl(page, pageRequest, count)
    }

    @Query(
        """
        SELECT * FROM ($SELECT_EXERCISES_BY_FILTERS_QUERY) as ex
        ORDER BY ex.title
        LIMIT :pageSize OFFSET :offset
    """,
        rowMapperClass = ExerciseDtoRowMapper::class
    )
    fun getExercisesByFilters(
        @Param("search") search: ExerciseSearchDto,
        offset: Long,
        pageSize: Int
    ): List<ExerciseDto>

    @Query(
        """
        SELECT count(*) 
        FROM ($SELECT_EXERCISES_BY_FILTERS_QUERY) as ex
    """
    )
    fun countExercises(
        @Param("search") search: ExerciseSearchDto,
    ): Long

    @Query(
        """
        SELECT id, name FROM exercise_types
    """
    )
    fun getExerciseTypes(): List<ExerciseType>
}

object ExerciseDtoRowMapper : RowMapper<ExerciseDto> {

    override fun mapRow(rs: ResultSet, rowNum: Int): ExerciseDto {
        return ExerciseDto(
            rs["id"],
            rs["title"],
            rs["description"],
            rs["indications"],
            rs["contradictions"],
            rs["duration"],
            ExerciseType(rs["type_id"], rs["type_title"]),
            rs["purposes"]
        )
    }

}

