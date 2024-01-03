package pro.qyoga.core.therapy.exercises.internal

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import pro.qyoga.core.therapy.exercises.api.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.api.ExerciseDto
import pro.qyoga.core.therapy.exercises.api.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.api.ExercisesService
import pro.qyoga.platform.file_storage.api.File
import pro.qyoga.platform.file_storage.api.FilesStorage


@Service
class ExercisesServiceImpl(
    private val exercisesRepo: ExercisesRepo,
    private val filesStorage: FilesStorage
) : ExercisesService {

    override fun addExercise(
        createExerciseRequest: CreateExerciseRequest,
        stepImages: Map<Int, File>,
        therapistId: Long
    ) {
        val persistedImages = stepImages
            .map { it.key to filesStorage.uploadFile(it.value) }
            .toMap()

        val exercise = Exercise.of(createExerciseRequest, persistedImages, therapistId)

        exercisesRepo.save(exercise)
    }

    override fun findExercises(searchDto: ExerciseSearchDto, page: Pageable): Page<ExerciseDto> {
        return exercisesRepo.findExercises(searchDto, page)
    }

    override fun addExercises(
        createExerciseRequests: List<Pair<CreateExerciseRequest, Map<Int, File>>>,
        therapistId: Long
    ): List<ExerciseDto> {
        val exercises = createExerciseRequests.map { Exercise.of(it.first, emptyMap(), therapistId) }
        return exercisesRepo.saveAll(exercises).map { it.toDto() }
    }

}