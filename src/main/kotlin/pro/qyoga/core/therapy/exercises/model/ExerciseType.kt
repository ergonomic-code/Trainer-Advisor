package pro.qyoga.core.therapy.exercises.model

import pro.qyoga.platform.kotlin.LabeledEnum


enum class ExerciseType(override val label: String) : LabeledEnum {

    WARM_UP("Разминка"),
    MOBILISATION("Мобилизация"),
    STRENGTHENING("Укрепление"),
    STRETCHING("Растяжка"),
    RELAXATION("Расслабление"),
    TRACTION("Тракция")

}