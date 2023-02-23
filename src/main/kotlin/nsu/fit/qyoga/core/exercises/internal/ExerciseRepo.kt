package nsu.fit.qyoga.core.exercises.internal

import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
interface ExerciseRepo : CrudRepository<Exercise, Long>, PagingAndSortingRepository<Exercise, Long> {

    @Query(
        """
        SELECT * FROM exercises ex
        INNER JOIN exercise_purposes ep ON ex.id = ep.exercise_id
        INNER JOIN therapeutic_purposes tp ON tp.id = ep.purpose_id
        INNER JOIN exercise_types et on ex.exercise_type_id = et.id
        WHERE (ex.title = :title OR :title IS NULL) 
        AND (ex.contradictions = :contradiction OR :contradiction IS NULL)
        AND (ex.duration = :duration OR :duration IS NULL)
        AND (et.name = :exerciseType OR :exerciseType IS NULL)
        AND (tp.purpose = :therapeuticPurpose OR :therapeuticPurpose IS NULL)
    """
    )
    fun getExercisesByFilters(
        title: String?,
        contradiction: String?,
        duration: String?,
        exerciseType: ExerciseType?,
        therapeuticPurpose: String?,
    ): List<Exercise>


    @Query(
        """
        SELECT et.id, et.name FROM exercise_types et
    """
    )
    fun getExerciseTypes(): List<ExerciseType>
}
