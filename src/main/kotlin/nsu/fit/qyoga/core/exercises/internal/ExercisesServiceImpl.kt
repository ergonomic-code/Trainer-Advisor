package nsu.fit.qyoga.core.exercises.internal

import nsu.fit.platform.errors.ResourceNotFound
import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.ImagesService
import nsu.fit.qyoga.core.exercises.api.dtos.*
import nsu.fit.qyoga.core.exercises.api.model.*
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

    override fun createExercise(modifiableExerciseDto: ModifiableExerciseDto, therapistId: Long): Exercise {
        val savedExercise = exercisesRepo.save(
            Exercise(
                title = modifiableExerciseDto.title!!,
                description = modifiableExerciseDto.description!!,
                indications = modifiableExerciseDto.indications!!,
                contradictions = modifiableExerciseDto.contradiction!!,
                duration = PGInterval(modifiableExerciseDto.duration!!),
                exerciseTypeId = modifiableExerciseDto.exerciseType!!.id,
                therapistId = therapistId
            )
        )

        val newPurpose = getTherapeuticPurpose(modifiableExerciseDto.therapeuticPurpose!!)
        newPurpose.addExercise(savedExercise)
        therapeuticPurposesRepo.save(newPurpose)

        saveOrUpdateExercisesSteps(modifiableExerciseDto, savedExercise)

        return savedExercise
    }

    override fun editExercise(modifiableExerciseDto: ModifiableExerciseDto): Exercise {
        val targetExercise =
            exercisesRepo.findByIdOrNull(modifiableExerciseDto.id!!)
                ?: throw ResourceNotFound("No existing exercise with id = ${modifiableExerciseDto.id}")

        val exercise = targetExercise.copy(
            title = modifiableExerciseDto.title!!,
            description = modifiableExerciseDto.description!!,
            indications = modifiableExerciseDto.indications!!,
            contradictions = modifiableExerciseDto.contradiction!!,
            duration = PGInterval(modifiableExerciseDto.duration),
            exerciseTypeId = modifiableExerciseDto.exerciseType!!.id
        )

        val purpose = getTherapeuticPurpose(modifiableExerciseDto.therapeuticPurpose!!)
        purpose.addExercise(exercise)
        therapeuticPurposesRepo.save(purpose)

        saveOrUpdateExercisesSteps(modifiableExerciseDto, exercise)

        return exercise
    }

    override fun getExerciseById(id: Long): ModifiableExerciseDto {
        val plainExercise =
            exercisesRepo.getExerciseByIdOrNull(id) ?: throw ResourceNotFound("No existing exercise with id = $id")

        val steps = exerciseStepsRepo.findAllByExerciseId(plainExercise.id)
        val exerciseStepsWithPhotos = steps.map {
            ExerciseStepDto(
                it.description,
                it.imageId?.let { imagesService.getImage(id)?.toMultipartFile() }
            )
        }.toMutableList()

        return ModifiableExerciseDto(
            id = plainExercise.id,
            title = plainExercise.title,
            description = plainExercise.description,
            indications = plainExercise.indications,
            contradiction = plainExercise.contradictions,
            duration = plainExercise.duration,
            exerciseType = plainExercise.type,
            therapeuticPurpose = plainExercise.purpose,
            exerciseSteps = exerciseStepsWithPhotos
        )
    }

    override fun getExerciseTypes(): List<ExerciseType> {
        return exercisesRepo.getExerciseTypes().map { ExerciseType.valueOf(it.name) }
    }

    private fun getTherapeuticPurpose(purpose: String): Purpose {
        val purposes = therapeuticPurposesRepo.findAll().filter { it.purpose == purpose }
        return if (purposes.isEmpty()) {
            Purpose(purpose = purpose, exercises = HashSet())
        } else {
            purposes[0]
        }
    }

    private fun saveOrUpdateExercisesSteps(modifiableExerciseDto: ModifiableExerciseDto, exercise: Exercise) {
        modifiableExerciseDto.exerciseSteps.map {
            var imageId: Long? = null
            if (it.photo != null) {
                imageId = imagesService.uploadImage(
                    ImageDto(
                        it.photo!!.name,
                        it.photo!!.contentType ?: "application/octet-stream",
                        it.photo!!.size,
                        it.photo!!.inputStream
                    )
                )
            }
            exerciseStepsRepo.save(
                ExerciseStep(
                    description = it.description,
                    imageId = imageId,
                    exerciseId = exercise.id
                )
            )
        }
    }
}
