package pro.qyoga.core.therapy.exercises.api

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import pro.qyoga.platform.file_storage.api.StoredFile
import java.io.InputStream

interface ExercisesService {

    fun addExercise(createExerciseRequest: CreateExerciseRequest, stepImages: Map<Int, StoredFile>, therapistId: Long)

    fun findExerciseSummaries(searchDto: ExerciseSearchDto, page: Pageable): Page<ExerciseSummaryDto>

    fun addExercises(
        createExerciseRequests: List<Pair<CreateExerciseRequest, Map<Int, StoredFile>>>,
        therapistId: Long
    ): List<ExerciseSummaryDto>

    fun getStepImage(exerciseId: Long, stepIdx: Int): InputStream?

}
