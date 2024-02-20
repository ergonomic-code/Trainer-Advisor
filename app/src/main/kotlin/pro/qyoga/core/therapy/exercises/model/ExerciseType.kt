package pro.qyoga.core.therapy.exercises.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import pro.azhidkov.platform.kotlin.LabeledEnum


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ExerciseType(override val label: String) : LabeledEnum {

    WARM_UP("Разминка"),
    MOBILISATION("Мобилизация"),
    STRENGTHENING("Укрепление"),
    STRETCHING("Растяжка"),
    RELAXATION("Расслабление"),
    TRACTION("Тракция");

    companion object {

        @JsonCreator
        @JvmStatic
        fun ofName(@JsonProperty("name") name: String?): ExerciseType? {
            return entries.find { it.name == name }
        }

    }

}