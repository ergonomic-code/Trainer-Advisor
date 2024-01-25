package pro.qyoga.core.therapy.exercises.model

import com.fasterxml.jackson.annotation.JsonFormat
import pro.qyoga.platform.kotlin.LabeledEnum


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ExerciseType(override val label: String) : LabeledEnum {

    WARM_UP("Разминка"),
    MOBILISATION("Мобилизация"),
    STRENGTHENING("Укрепление"),
    STRETCHING("Растяжка"),
    RELAXATION("Расслабление"),
    TRACTION("Тракция")

}