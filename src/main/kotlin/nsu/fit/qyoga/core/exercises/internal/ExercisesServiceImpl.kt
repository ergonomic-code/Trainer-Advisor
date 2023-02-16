package nsu.fit.qyoga.core.exercises.internal

import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class ExercisesServiceImpl(
    private val exerciseRepo: ExerciseRepo
) : ExercisesService {

    override fun getExercises(
        title: String?,
        contradiction: String?,
        duration: String?,
        exerciseType: ExerciseType?,
        therapeuticPurpose: String?
    ): Page<Exercise> {
        return exerciseRepo.getExercisesByFilters(title, contradiction, duration, exerciseType, therapeuticPurpose)
    }
}