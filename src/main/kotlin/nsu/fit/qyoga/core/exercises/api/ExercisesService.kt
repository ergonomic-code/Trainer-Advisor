package nsu.fit.qyoga.core.exercises.api

import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface ExercisesService {
    fun getExercises(
        title: String?,
        contradiction: String?,
        duration: String?,
        exerciseType: ExerciseType?,
        therapeuticPurpose: String?,
        pageable: Pageable
    ): PageImpl<ExerciseDto>

    fun getExerciseTypes(): List<ExerciseType>
}
