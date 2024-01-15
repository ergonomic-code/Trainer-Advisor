package pro.qyoga.core.therapy.exercises.api

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import pro.qyoga.core.therapy.exercises.api.dtos.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.api.dtos.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.api.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.exercises.api.model.Exercise
import pro.qyoga.platform.file_storage.api.StoredFile
import pro.qyoga.platform.file_storage.api.StoredFileInputStream

interface ExercisesService {

    fun addExercise(
        createExerciseRequest: CreateExerciseRequest,
        stepImages: Map<Int, StoredFile>,
        therapistId: Long
    ): Exercise

    fun findById(exerciseId: Long): Exercise?

    fun findExerciseSummaries(searchDto: ExerciseSearchDto, page: Pageable): Page<ExerciseSummaryDto>

    fun updateExercise(exerciseId: Long, exerciseSummaryDto: ExerciseSummaryDto)

    fun addExercises(
        createExerciseRequests: List<Pair<CreateExerciseRequest, Map<Int, StoredFile>>>,
        therapistId: Long
    ): Iterable<Exercise>

    fun getStepImage(exerciseId: Long, stepIdx: Int): StoredFileInputStream?

    fun deleteById(exerciseId: Long)

}
