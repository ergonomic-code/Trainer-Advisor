package nsu.fit.qyoga.core.exercises.internal

import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.ImagesService
import nsu.fit.qyoga.core.exercises.api.dtos.CreateExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.exercises.api.dtos.ImageDto
import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.core.exercises.api.model.ExerciseStep
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ExercisesServiceImpl(
    private val exercisesRepo: ExercisesRepo,
    private val exerciseStepsRepo: ExerciseStepsRepo,
    private val imagesService: ImagesService
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

    override fun createExercise(createExerciseDto: CreateExerciseDto, therapistId: Long): Exercise {
        val savedExercise = exercisesRepo.insertExercise(createExerciseDto, therapistId)
        createExerciseDto.exerciseSteps.map {
            val imageId = imagesService.uploadImage(
                ImageDto(
                    it.photo?.name,
                    it.photo?.contentType ?: "application/octet-stream",
                    it.photo!!.size,
                    it.photo.inputStream
                )
            )
            exerciseStepsRepo.save(
                ExerciseStep(
                    description = it.description,
                    imageId = imageId,
                    exerciseId = savedExercise.id
                )
            )
        }
        return savedExercise
    }

    override fun getExerciseTypes(): List<ExerciseType> {
        return exercisesRepo.getExerciseTypes().map { ExerciseType.valueOf(it.name) }
    }
}
