package pro.qyoga.core.therapy.exercises.internal

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
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
import pro.qyoga.platform.spring.tx.TransactionalService


@Service
class ExercisesServiceImpl(
    private val exercisesRepo: ExercisesRepo,
    private val exerciseStepsImagesStorage: FilesStorage,
    override val transactionTemplate: TransactionTemplate
) : ExercisesService, TransactionalService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun addExercise(
        createExerciseRequest: CreateExerciseRequest,
        stepImages: Map<Int, StoredFile>,
        therapistId: Long
    ): Exercise {
        val stepIdxToStepImageId = exerciseStepsImagesStorage.uploadAllStepImages(stepImages)
            .mapValues { it.value.id }

        val exercise = Exercise.of(createExerciseRequest, stepIdxToStepImageId, therapistId)

        return exercisesRepo.save(exercise)
    }

    override fun findById(exerciseId: Long): Exercise? =
        exercisesRepo.findByIdOrNull(exerciseId)

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

    override fun deleteById(exerciseId: Long) {
        val exercise = transaction {
            val exercise = findById(exerciseId)
                ?: throw ExerciseNotFound(exerciseId)
            exercisesRepo.deleteById(exercise.id)
            exercise
        }

        try {
            val stepImageIds = exercise.steps.mapNotNull { it.imageId?.id }
            exerciseStepsImagesStorage.deleteAllById(stepImageIds)
        } catch (ex: Exception) {
            log.warn("Exercise images deletion failed", ex)
        }
    }

}

fun FilesStorage.uploadAllStepImages(stepImages: Map<Int, StoredFile>): Map<Int, FileMetaData> {
    val (stepIndexes, stepImageFiles) = stepImages.unzip()

    val persistedImages = uploadAll(stepImageFiles)

    val stepIdxToStepImage: Map<Int, FileMetaData> = stepIndexes.zip(persistedImages)
        .associate { (stepIdx, persistedImage) -> stepIdx to persistedImage }

    return stepIdxToStepImage
}