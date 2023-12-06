package pro.qyoga.core.therapy.exercises.api

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import pro.qyoga.infra.images.api.Image

interface ExercisesService {

    fun addExercise(createExerciseRequest: CreateExerciseRequest, stepImages: Map<Int, Image>, therapistId: Long)

    fun findExercises(searchDto: ExerciseSearchDto, page: Pageable): Page<ExerciseDto>


    fun addExercises(
        createExerciseRequests: List<Pair<CreateExerciseRequest, Map<Int, Image>>>,
        therapistId: Long
    ): List<ExerciseDto>

}
