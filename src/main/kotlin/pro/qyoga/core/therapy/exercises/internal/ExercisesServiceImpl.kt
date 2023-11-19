package pro.qyoga.core.therapy.exercises.internal

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.core.therapy.exercises.api.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.api.ExerciseDto
import pro.qyoga.core.therapy.exercises.api.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.api.ExercisesService
import pro.qyoga.platform.file_storage.api.FileMetaData
import pro.qyoga.platform.file_storage.api.FilesStorage
import pro.qyoga.platform.file_storage.api.StoredFile
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

    override fun findExercises(searchDto: ExerciseSearchDto, page: Pageable): Page<ExerciseDto> {
        return exercisesRepo.findExercises(searchDto, page)
    }

    @Transactional
    override fun addExercises(
        createExerciseRequests: List<Pair<CreateExerciseRequest, Map<Int, StoredFile>>>,
        therapistId: Long
    ): List<ExerciseDto> {
        val exercises = createExerciseRequests.map { Exercise.of(it.first, emptyMap(), therapistId) }
        return exercisesRepo.saveAll(exercises).map { it.toDto() }
    }

}

fun FilesStorage.uploadAllStepImages(stepImages: Map<Int, StoredFile>): Map<Int, FileMetaData> {
    val (stepIndexes, stepImageFiles) = stepImages.unzip()

    val persistedImages = uploadAll(stepImageFiles)

    val stepIdxToStepImage: Map<Int, FileMetaData> = stepIndexes.zip(persistedImages)
        .associate { (stepIdx, persistedImage) -> stepIdx to persistedImage }

    return stepIdxToStepImage
}