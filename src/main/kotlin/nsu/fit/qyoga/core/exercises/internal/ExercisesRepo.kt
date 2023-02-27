package nsu.fit.qyoga.core.exercises.internal

import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseTypeDto
import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = false)
interface ExercisesRepo : CrudRepository<Exercise, Long>, PagingAndSortingRepository<Exercise, Long> {

    @Query(
        """
        SELECT ex.id as id, title, description, indications, contradictions, duration, et.name as type, tp.purpose as purpose FROM exercises ex
            INNER JOIN exercise_purposes ep ON ex.id = ep.exercise_id
            INNER JOIN therapeutic_purposes tp ON tp.id = ep.purpose_id
            INNER JOIN exercise_types et on ex.exercise_type_id = et.id
        WHERE (ex.title LIKE '%' || :title || '%' OR :title IS NULL) 
            AND (ex.contradictions LIKE '%' || :contradiction || '%' OR :contradiction IS NULL)
            AND (ex.duration LIKE '%' || :duration || '%' OR :duration IS NULL)
            AND (et.name LIKE '%' || :exerciseType || '%' OR :exerciseType IS NULL)
            AND (tp.purpose LIKE '%' || :therapeuticPurpose || '%' OR :therapeuticPurpose IS NULL)
        LIMIT :pageSize OFFSET :offset
    """
    )
    fun getExercisesByFilters(
        title: String?,
        contradiction: String?,
        duration: String?,
        exerciseType: ExerciseType?,
        therapeuticPurpose: String?,
        offset: Int,
        pageSize: Int
    ): List<ExerciseDto>

    @Query(
        """
        SELECT count(*) FROM exercises ex
            INNER JOIN exercise_purposes ep ON ex.id = ep.exercise_id
            INNER JOIN therapeutic_purposes tp ON tp.id = ep.purpose_id
            INNER JOIN exercise_types et on ex.exercise_type_id = et.id
        WHERE (ex.title LIKE '%' || :title || '%' OR :title IS NULL) 
            AND (ex.contradictions LIKE '%' || :contradiction || '%' OR :contradiction IS NULL)
            AND (ex.duration LIKE '%' || :duration || '%' OR :duration IS NULL)
            AND (et.name LIKE '%' || :exerciseType || '%' OR :exerciseType IS NULL)
            AND (tp.purpose LIKE '%' || :therapeuticPurpose || '%' OR :therapeuticPurpose IS NULL)
    """
    )
    fun countExercises(
        title: String?,
        contradiction: String?,
        duration: String?,
        exerciseType: ExerciseType?,
        therapeuticPurpose: String?,
    ): Long

    @Query(
        """
        SELECT id, name FROM exercise_types
    """
    )
    fun getExerciseTypes(): List<ExerciseTypeDto>
}
