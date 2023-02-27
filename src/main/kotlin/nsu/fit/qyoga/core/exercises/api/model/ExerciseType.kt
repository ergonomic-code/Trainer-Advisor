package nsu.fit.qyoga.core.exercises.api.model

enum class ExerciseType(val id: Int, val title: String) {
    WarmUp(id = 1, title = "Разминка"),
    Mobilization(id = 2, title = "Мобилизация"),
    Strengthening(id = 3, title = "Укрепление"),
    Stretching(id = 4, title = "Вытяжение"),
    Relaxation(id = 5, title = "Расслабление"),
    Traction(id = 6, title = "Тракция")
}
