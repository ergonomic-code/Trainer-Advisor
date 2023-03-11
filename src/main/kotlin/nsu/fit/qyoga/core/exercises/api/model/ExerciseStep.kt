package nsu.fit.qyoga.core.exercises.api.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("exercise_steps")
data class ExerciseStep(
    @Id
    val id: Long,
    val description: String,
    val photo: ByteArray,
    val exerciseId: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExerciseStep

        if (id != other.id) return false
        if (description != other.description) return false
        if (!photo.contentEquals(other.photo)) return false
        if (exerciseId != other.exerciseId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + exerciseId.hashCode()
        return result
    }
}