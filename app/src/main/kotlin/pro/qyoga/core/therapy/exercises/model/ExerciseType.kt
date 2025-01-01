package pro.qyoga.core.therapy.exercises.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.JsonNode
import pro.azhidkov.platform.kotlin.LabeledEnum
import pro.qyoga.core.therapy.exercises.model.ExerciseType.entries


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
        fun ofName(node: JsonNode): ExerciseType? {
            require(node.isTextual || node.isObject) { "Создать экземпляр ExerciseType можно только из строки или объекта" }
            val name = if (node.isTextual) node.textValue() else node["name"].asText()
            return entries.find { it.name == name }
        }

    }

}