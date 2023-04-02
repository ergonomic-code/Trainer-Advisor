package nsu.fit.qyoga.core.exercises.api.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table

@Table("therapeutic_purposes")
data class Purpose(
    @Id
    val id: Long = 0,
    val purpose: String?,
    @MappedCollection(idColumn = "purpose_id")
    val exercises: MutableSet<ExerciseRef> = HashSet()
) {
    fun addExercise(exercise: Exercise) {
        exercises.add(ExerciseRef(exercise.id))
    }
}