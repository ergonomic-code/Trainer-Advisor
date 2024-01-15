package pro.qyoga.core.therapy.exercises.internal

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.core.therapy.exercises.api.ExercisesService
import pro.qyoga.core.therapy.exercises.api.dtos.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.api.dtos.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.api.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.exercises.api.errors.ExerciseNotFound
import pro.qyoga.core.therapy.exercises.api.errors.ExerciseStepNotFound
import pro.qyoga.core.therapy.exercises.api.model.Exercise
import pro.qyoga.platform.file_storage.api.FileMetaData
import pro.qyoga.platform.file_storage.api.FilesStorage
import pro.qyoga.platform.file_storage.api.StoredFile
import pro.qyoga.platform.file_storage.api.StoredFileInputStream
import pro.qyoga.platform.kotlin.unzip


@Service
class ExercisesServiceImpl(
    private val exercisesRepo: ExercisesRepo,
    private val exerciseStepsImagesStorage: FilesStorage
) : ExercisesService {

    @Transactional
    override fun addExercise(
        createExerciseRequest: CreateExerciseRequest,
        stepImages: Map<Int, StoredFile>,
        therapistId: Long
    ) {
        val stepIdxToStepImageId = exerciseStepsImagesStorage.uploadAllStepImages(stepImages)
            .mapValues { it.value.id }

        val exercise = Exercise.of(createExerciseRequest, stepIdxToStepImageId, therapistId)

        exercisesRepo.save(exercise)
    }

    override fun findById(exerciseId: Long): Exercise =
        exercisesRepo.findByIdOrNull(exerciseId)
            ?: throw ExerciseNotFound(exerciseId)

    override fun findExerciseSummaries(searchDto: ExerciseSearchDto, page: Pageable): Page<ExerciseSummaryDto> {
        return exercisesRepo.findExerciseSummaries(searchDto, page)
    }

    override fun updateExercise(exerciseId: Long, exerciseSummaryDto: ExerciseSummaryDto) {
        exercisesRepo.update(exerciseId) {
            it.patchBy(exerciseSummaryDto)
        }
    }

    @Transactional
    override fun addExercises(
        createExerciseRequests: List<Pair<CreateExerciseRequest, Map<Int, StoredFile>>>,
        therapistId: Long
    ): Iterable<Exercise> {
        val exercises = createExerciseRequests.map { (request, images) ->
            val storedImages = exerciseStepsImagesStorage.uploadAllStepImages(images)
                .mapValues { it.value.id }
            Exercise.of(request, storedImages, therapistId)
        }
        return exercisesRepo.saveAll(exercises)
    }

    override fun getStepImage(exerciseId: Long, stepIdx: Int): StoredFileInputStream? {
        val exercise = exercisesRepo.findByIdOrNull(exerciseId)
            ?: throw ExerciseNotFound(exerciseId)

        if (stepIdx >= exercise.steps.size) {
            throw ExerciseStepNotFound(exerciseId, stepIdx, exercise.steps.size)
        }

        val imageId = exercise.steps[stepIdx].imageId
            ?: return null

        return exerciseStepsImagesStorage.findByIdOrNull(imageId.id!!)
    }

}

fun FilesStorage.uploadAllStepImages(stepImages: Map<Int, StoredFile>): Map<Int, FileMetaData> {
    val (stepIndexes, stepImageFiles) = stepImages.unzip()

    val persistedImages = uploadAll(stepImageFiles)

    val stepIdxToStepImage: Map<Int, FileMetaData> = stepIndexes.zip(persistedImages)
        .associate { (stepIdx, persistedImage) -> stepIdx to persistedImage }

    return stepIdxToStepImage
}