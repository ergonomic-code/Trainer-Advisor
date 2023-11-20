package pro.qyoga.core.programs.exercises.internal

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import pro.qyoga.core.programs.exercises.api.CreateExerciseRequest
import pro.qyoga.core.programs.exercises.api.ExerciseDto
import pro.qyoga.core.programs.exercises.api.ExerciseSearchDto
import pro.qyoga.core.programs.exercises.api.ExercisesService
import pro.qyoga.infra.images.api.Image
import pro.qyoga.infra.images.api.ImagesService


@Service
class ExercisesServiceImpl(
    private val exercisesRepo: ExercisesRepo,
    private val imagesService: ImagesService
) : ExercisesService {

    override fun addExercise(
        createExerciseRequest: CreateExerciseRequest,
        stepImages: Map<Int, Image>,
        therapistId: Long
    ) {
        val persistedImages = stepImages
            .map { it.key to imagesService.uploadImage(it.value) }
            .toMap()

        val exercise = Exercise.of(createExerciseRequest, persistedImages, therapistId)

        exercisesRepo.save(exercise)
    }

    override fun findExercises(searchDto: ExerciseSearchDto, page: Pageable): Page<ExerciseDto> {
        return exercisesRepo.findExercises(searchDto, page)
    }

    override fun addExercises(
        createExerciseRequests: List<Pair<CreateExerciseRequest, Map<Int, Image>>>,
        therapistId: Long
    ): List<ExerciseDto> {
        val exercises = createExerciseRequests.map { Exercise.of(it.first, emptyMap(), therapistId) }
        return exercisesRepo.saveAll(exercises).map { it.toDto() }
    }

}