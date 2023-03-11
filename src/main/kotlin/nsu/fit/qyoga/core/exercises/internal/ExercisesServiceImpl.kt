package nsu.fit.qyoga.core.exercises.internal

import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
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
            searchDto,
            pageable.pageNumber * pageable.pageSize,
            pageable.pageSize
        )
        val count = exercisesRepo.countExercises(searchDto)
        return PageImpl(result, pageable, count)
    }

    override fun getExerciseTypes(): List<ExerciseType> {
        return exercisesRepo.getExerciseTypes().map { ExerciseType.valueOf(it.name) }
    }
}
