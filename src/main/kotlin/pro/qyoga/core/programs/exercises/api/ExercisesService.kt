package pro.qyoga.core.programs.exercises.api

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import pro.qyoga.infra.files.api.File

interface ExercisesService {

    fun addExercise(createExerciseRequest: CreateExerciseRequest, stepImages: Map<Int, File>, therapistId: Long)

    fun findExercises(searchDto: ExerciseSearchDto, page: Pageable): Page<ExerciseDto>


    fun addExercises(
        createExerciseRequests: List<Pair<CreateExerciseRequest, Map<Int, File>>>,
        therapistId: Long
    ): List<ExerciseDto>

}
