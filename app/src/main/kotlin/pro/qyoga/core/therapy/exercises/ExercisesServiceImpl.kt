package pro.qyoga.core.therapy.exercises

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import pro.azhidkov.platform.file_storage.api.FileMetaData
import pro.azhidkov.platform.file_storage.api.FilesStorage
import pro.azhidkov.platform.file_storage.api.StoredFile
import pro.azhidkov.platform.file_storage.api.StoredFileInputStream
import pro.azhidkov.platform.kotlin.unzip
import pro.azhidkov.platform.spring.tx.TransactionalService
import pro.qyoga.core.therapy.exercises.dtos.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.exercises.impl.ExercisesRepo
import pro.qyoga.core.therapy.exercises.model.Exercise
import java.util.*


@Service
class ExercisesService(
    private val exercisesRepo: ExercisesRepo,
    private val exerciseStepsImagesStorage: FilesStorage,
    override val transactionTemplate: TransactionTemplate
) : TransactionalService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun addExercise(
        createExerciseRequest: CreateExerciseRequest,
        stepImages: Map<Int, StoredFile>,
        therapistId: UUID
    ): Exercise {
        val stepIdxToStepImageId = exerciseStepsImagesStorage.uploadAllStepImages(stepImages)
            .mapValues { it.value.id }

        val exercise = Exercise.of(createExerciseRequest, stepIdxToStepImageId, therapistId)

        return exercisesRepo.save(exercise)
    }

    fun findById(exerciseId: Long): Exercise? =
        exercisesRepo.findByIdOrNull(exerciseId)

    fun findExerciseSummaries(searchDto: ExerciseSearchDto, page: Pageable): Page<ExerciseSummaryDto> {
        return exercisesRepo.findExerciseSummaries(searchDto, page)
    }

    fun updateExercise(exerciseId: Long, exerciseSummaryDto: ExerciseSummaryDto) {
        exercisesRepo.updateById(exerciseId) {
            it.patchBy(exerciseSummaryDto)
        }
    }

    @Transactional
    fun addExercises(
        createExerciseRequests: List<Pair<CreateExerciseRequest, Map<Int, StoredFile>>>,
        therapistId: UUID
    ): Iterable<Exercise> {
        val exercises = createExerciseRequests.map { (request, images) ->
            val storedImages = exerciseStepsImagesStorage.uploadAllStepImages(images)
                .mapValues { it.value.id }
            Exercise.of(request, storedImages, therapistId)
        }
        return exercisesRepo.saveAll(exercises)
    }

    fun getStepImage(exerciseId: Long, stepIdx: Int): StoredFileInputStream? {
        val exercise = exercisesRepo.findByIdOrNull(exerciseId)
            ?: return null

        if (stepIdx >= exercise.steps.size) {
            return null
        }

        val imageId = exercise.steps[stepIdx].imageId
            ?: return null

        return exerciseStepsImagesStorage.findByIdOrNull(imageId.id!!)
    }

    fun deleteById(exerciseId: Long) {
        val exercise = transaction {
            val exercise = findById(exerciseId)
                ?: return@transaction null

            exercisesRepo.deleteById(exercise.id)
            return@transaction exercise
        }

        if (exercise == null) {
            return
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