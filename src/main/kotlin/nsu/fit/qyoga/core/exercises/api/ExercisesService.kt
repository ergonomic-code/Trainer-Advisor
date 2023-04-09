package nsu.fit.qyoga.core.exercises.api

import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.exercises.api.dtos.ModifiableExerciseDto
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
        modifiableExerciseDto: ModifiableExerciseDto,
        therapistId: Long
    ): Exercise

    fun editExercise(
        modifiableExerciseDto: ModifiableExerciseDto
    ): Exercise

    fun getExerciseById(id: Long): ModifiableExerciseDto

    fun getExerciseTypes(): List<ExerciseType>
}
