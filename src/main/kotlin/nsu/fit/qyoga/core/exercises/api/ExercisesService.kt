package nsu.fit.qyoga.core.exercises.api

import nsu.fit.qyoga.core.exercises.api.dtos.CreateExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ExercisesService {
    fun getExercises(
        searchDto: ExerciseSearchDto,
        pageable: Pageable
    ): Page<ExerciseDto>

    fun createExercise(
        createExerciseDto: CreateExerciseDto,
        therapistId: Long
    ): Exercise

    fun editExercise(
        exerciseDto: ExerciseDto
    ): Exercise

    fun getExerciseById(id: Long): ExerciseDto

    fun getExerciseTypes(): List<ExerciseType>
}
