package nsu.fit.qyoga.core.exercises.internal

import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import nsu.fit.qyoga.core.exercises.utils.pages.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ExercisesServiceImpl(
    private val exercisesRepo: ExercisesRepo,
) : ExercisesService {

    override fun getExercises(
        searchDto: ExerciseSearchDto,
        pageable: Pageable
    ): Page<ExerciseDto> {
        val result = exercisesRepo.getExercisesByFilters(
            searchDto.title,
            searchDto.contradiction,
            searchDto.duration,
            searchDto.exerciseType,
            searchDto.therapeuticPurpose,
            pageable.pageNumber * pageable.pageSize,
            pageable.pageSize
        )
        val count = exercisesRepo.countExercises(
            searchDto.title,
            searchDto.contradiction,
            searchDto.duration,
            searchDto.exerciseType,
            searchDto.therapeuticPurpose,
        )
        return Page(result, pageable.pageNumber, pageable.pageSize, count)
    }

    override fun getExerciseTypes(): List<ExerciseType> {
        return exercisesRepo.getExerciseTypes().map { ExerciseType.valueOf(it.name) }
    }
}
