package pro.qyoga.fixture.programs.exercises

import pro.qyoga.core.programs.exercises.api.*
import pro.qyoga.fixture.data.randomCyrillicWord
import pro.qyoga.fixture.data.randomListIndexed
import pro.qyoga.fixture.data.randomMinutesDuration
import pro.qyoga.fixture.data.randomSentence
import pro.qyoga.platform.java.time.toDecimalMinutes
import pro.qyoga.platform.java.time.toDurationMinutes
import java.time.Duration


object ExercisesObjectMother {

    fun createExerciseRequest(
        title: String = randomCyrillicWord(),
        description: String = randomSentence(),
        duration: Duration = randomExerciseDuration(),
        exerciseType: ExerciseType = randomExerciseTypeId(),
        steps: () -> List<ExerciseStepDto> = { randomListIndexed(1, 5) { idx -> exerciseStepDto(idx) } }
    ) = CreateExerciseRequest(
        title,
        description,
        duration.toDecimalMinutes(),
        exerciseType,
        steps()
    )


    fun exerciseStepDto(idx: Int, description: String = randomSentence()) =
        ExerciseStepDto(idx.toLong(), description, 0)

    fun exerciseSearchDto(title: String) = ExerciseSearchDto(title = title)

    fun createExerciseRequests(count: Int): List<CreateExerciseRequest> =
        (1..count).map { createExerciseRequest() }

}

fun CreateExerciseRequest.toDto() =
    ExerciseDto(0, title, description, duration.toDurationMinutes(), exerciseType)

fun randomExerciseDuration(): Duration = randomMinutesDuration(4, 30)

fun randomExerciseTypeId() = ExerciseType.entries.random()
