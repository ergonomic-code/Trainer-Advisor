package nsu.fit.qyoga.core.exercises.internal

import nsu.fit.platform.web.pages.Page
import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ExercisesServiceImpl(
    private val exercisesRepo: ExercisesRepo,
) : ExercisesService {

    override fun getExercises(
        title: String?,
        contradiction: String?,
        duration: String?,
        exerciseType: ExerciseType?,
        therapeuticPurpose: String?,
        pageable: Pageable
    ): Page<ExerciseDto> {
        val result = exercisesRepo.getExercisesByFilters(
            title,
            contradiction,
            duration,
            exerciseType,
            therapeuticPurpose,
            pageable.pageNumber,
            pageable.pageSize
        )
        val count = exercisesRepo.countExercises(title, contradiction, duration, exerciseType, therapeuticPurpose)
        return Page(result, pageable.pageNumber, pageable.pageSize, count)
    }

    override fun getExerciseTypes(): List<ExerciseType> {
        return exercisesRepo.getExerciseTypes().map { ExerciseType.valueOf(it.name) }
    }
}
