package nsu.fit.qyoga.core.exercises.internal

import nsu.fit.platform.lang.toDurationMinutes
import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.dtos.CreateExerciseRequest
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.core.exercises.api.model.ExercisePurpose
import nsu.fit.qyoga.core.exercises.api.model.ExerciseStep
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import nsu.fit.qyoga.core.images.api.ImageService
import nsu.fit.qyoga.core.images.api.model.Image
import nsu.fit.qyoga.core.therapeutic_purposes.api.TherapeuticPurpose
import nsu.fit.qyoga.core.therapeutic_purposes.api.TherapeuticPurposesService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExercisesServiceImpl(
        private val exercisesRepo: ExercisesRepo,
        private val therapeuticPurposesService: TherapeuticPurposesService,
        private val imagesService: ImageService
) : ExercisesService {

    override fun getExercises(
            searchDto: ExerciseSearchDto,
            pageable: Pageable
    ): Page<ExerciseDto> {
        return exercisesRepo.getExercisesByFilters(searchDto, pageable)
    }

    override fun getExerciseTypes(): List<ExerciseType> {
        return exercisesRepo.getExerciseTypes()
    }

    @Transactional
    override fun addExercise(exercise: Exercise, stepImages: Map<Int, Image>) {
        val persistedImages = stepImages
                .map { it.key to imagesService.uploadImage(it.value) }
                .toMap()

        val exerciseWithImages = exercise.mapSteps {
            it.copy(imageId = persistedImages[it.stepIndex])
        }
        exercisesRepo.save(exerciseWithImages)
    }

    @Transactional
    override fun addExercise(
            createExerciseRequest: CreateExerciseRequest,
            stepImages: Map<Int, Image>,
            userId: Long,
            chosenPurposes: List<TherapeuticPurpose>
    ) {
        val persistedImages = stepImages
                .map { it.key to imagesService.uploadImage(it.value) }
                .toMap()

        val exerciseSteps = createExerciseRequest.steps.mapIndexed { idx, dto ->
            ExerciseStep(dto.description, persistedImages[idx], idx)
        }

        val persistedPurposes = therapeuticPurposesService.saveAll(chosenPurposes)
        val exercisePurposes = persistedPurposes.map(::ExercisePurpose)

        val exercise = Exercise(
                createExerciseRequest.title,
                createExerciseRequest.description,
                createExerciseRequest.indications,
                createExerciseRequest.contradictions,
                createExerciseRequest.duration.toDurationMinutes(),
                createExerciseRequest.exerciseTypeId,
                userId,
                exercisePurposes.toSet(),
                exerciseSteps
        )

        exercisesRepo.save(exercise)
    }

}
