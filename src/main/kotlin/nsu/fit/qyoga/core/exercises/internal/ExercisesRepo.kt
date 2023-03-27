package nsu.fit.qyoga.core.exercises.internal

import nsu.fit.qyoga.core.exercises.api.dtos.CreateExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseTypeDto
import nsu.fit.qyoga.core.exercises.api.model.Exercise
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = false)
interface ExercisesRepo : CrudRepository<Exercise, Long>, PagingAndSortingRepository<Exercise, Long> {

    @Query(
        """
        SELECT ex.id as id, title, description, indications, contradictions, duration::varchar, et.name as type, tp.purpose as purpose
        FROM exercises ex
            INNER JOIN exercise_purposes ep ON ex.id = ep.exercise_id
            INNER JOIN therapeutic_purposes tp ON tp.id = ep.purpose_id
            INNER JOIN exercise_types et on ex.exercise_type_id = et.id
        WHERE (ex.title LIKE '%' || :#{#search.title ?: ""} || '%' OR :#{#search.title ?: ""} IS NULL)
            AND (ex.contradictions LIKE '%' || :#{#search?.contradiction ?: ""} || '%' OR :#{#search.contradiction ?: ""} IS NULL)
            AND (et.name LIKE '%' || :#{#search.exerciseType?.name ?: ""} || '%' OR :#{#search.exerciseType?.name ?: ""} IS NULL)
            AND (tp.purpose LIKE '%' || :#{#search.therapeuticPurpose ?: ""} || '%' OR :#{#search.therapeuticPurpose ?: ""} IS NULL)
        ORDER BY ex.title
        LIMIT :pageSize OFFSET :offset
    """
    )
    fun getExercisesByFilters(
        @Param("search") search: ExerciseSearchDto,
        offset: Int,
        pageSize: Int
    ): List<ExerciseDto>

    @Query(
        """
        SELECT count(*) 
        FROM exercises ex
            INNER JOIN exercise_purposes ep ON ex.id = ep.exercise_id
            INNER JOIN therapeutic_purposes tp ON tp.id = ep.purpose_id
            INNER JOIN exercise_types et on ex.exercise_type_id = et.id
        WHERE (ex.title LIKE '%' || :#{#search.title ?: ""} || '%' OR :#{#search.title ?: ""} IS NULL)
            AND (ex.contradictions LIKE '%' || :#{#search?.contradiction ?: ""} || '%' OR :#{#search.contradiction ?: ""} IS NULL)
            AND (et.name LIKE '%' || :#{#search.exerciseType?.name ?: ""} || '%' OR :#{#search.exerciseType?.name ?: ""} IS NULL)
            AND (tp.purpose LIKE '%' || :#{#search.therapeuticPurpose ?: ""} || '%' OR :#{#search.therapeuticPurpose ?: ""} IS NULL)
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
    fun getExerciseTypes(): List<ExerciseTypeDto>

    @Query(
        """
            INSERT INTO exercises(title, description, indications, contradictions, duration, exercise_type_id, therapist_id)
            VALUES (:#{#create?.title}, :#{#create?.description}, :#{#create?.indications}, :#{#create?.contradiction}, :#{#create?.duration}::interval, :#{#create?.exerciseType?.id}, :therapistId) 
        """
    )
    fun insertExercise(create: CreateExerciseDto, therapistId: Long): Exercise
}
