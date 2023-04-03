package nsu.fit.qyoga.core.exercises.internal

import nsu.fit.platform.errors.ResourceNotFound
import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.ImagesService
import nsu.fit.qyoga.core.exercises.api.dtos.CreateExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.exercises.api.dtos.ImageDto
import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.core.exercises.api.model.ExerciseStep
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import nsu.fit.qyoga.core.exercises.api.model.Purpose
import org.postgresql.util.PGInterval
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ExercisesServiceImpl(
    private val exercisesRepo: ExercisesRepo,
    private val exerciseStepsRepo: ExerciseStepsRepo,
    private val imagesService: ImagesService,
    private val therapeuticPurposesRepo: TherapeuticPurposesRepo
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
        val savedExercise = exercisesRepo.save(
            Exercise(
                title = createExerciseDto.title!!,
                description = createExerciseDto.description!!,
                indications = createExerciseDto.indications!!,
                contradictions = createExerciseDto.contradiction!!,
                duration = PGInterval(createExerciseDto.duration!!),
                exerciseTypeId = createExerciseDto.exerciseType!!.id,
                therapistId = therapistId
            )
        )
        val purposes = therapeuticPurposesRepo.findAll().filter { it.purpose == createExerciseDto.therapeuticPurpose }
        val newPurpose: Purpose = if (purposes.isEmpty()) {
            Purpose(purpose = createExerciseDto.therapeuticPurpose, exercises = HashSet())
        } else {
            purposes[0]
        }

        newPurpose.addExercise(savedExercise)
        therapeuticPurposesRepo.save(newPurpose)
        createExerciseDto.exerciseSteps.map {
            var imageId: Long? = null
            if (it.photo != null) {
                imageId = imagesService.uploadImage(
                    ImageDto(
                        it.photo.name,
                        it.photo.contentType ?: "application/octet-stream",
                        it.photo.size,
                        it.photo.inputStream
                    )
                )
            }
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

    override fun editExercise(exerciseDto: ExerciseDto): Exercise {
        val exerciseToEdit =
            exercisesRepo.findByIdOrNull(exerciseDto.id) ?: throw ResourceNotFound("No existing exercise with this id")

    }

    override fun getExerciseById(id: Long): ExerciseDto {
        return exercisesRepo.getByIdOrNull(id) ?: throw ResourceNotFound("No existing exercise with this id")
    }

    override fun getExerciseTypes(): List<ExerciseType> {
        return exercisesRepo.getExerciseTypes().map { ExerciseType.valueOf(it.name) }
    }
}
