package nsu.fit.qyoga.core.exercises.api.model

enum class ExerciseType(val id: Long, val value: String) {
    ResistanceBreathing(1, "ResistanceBreathing"),
    Balance(2, "Balance"),
    Stretching(3, "Stretching"),
    WeightBearingExercise(4, "WeightBearingExercise"),
    StrengthTraining(5, "StrengthTraining"),
    CardioTraining(6, "CardioTraining")
}
