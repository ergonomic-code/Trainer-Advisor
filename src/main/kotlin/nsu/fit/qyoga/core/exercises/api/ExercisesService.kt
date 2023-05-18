package nsu.fit.qyoga.core.exercises.api

import nsu.fit.qyoga.core.exercises.api.dtos.CreateExerciseRequest
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import nsu.fit.qyoga.core.images.api.model.Image
import nsu.fit.qyoga.core.therapeutic_purposes.api.TherapeuticPurpose
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ExercisesService {

    fun getExercises(
            searchDto: ExerciseSearchDto,
            pageable: Pageable
    ): Page<ExerciseDto>

    fun getExerciseTypes(): List<ExerciseType>

    fun addExercise(exercise: Exercise, stepImages: Map<Int, Image>)

    fun addExercise(
            createExerciseRequest: CreateExerciseRequest,
            stepImages: Map<Int, Image>,
            userId: Long,
            chosenPurposes: List<TherapeuticPurpose>
    )

}
