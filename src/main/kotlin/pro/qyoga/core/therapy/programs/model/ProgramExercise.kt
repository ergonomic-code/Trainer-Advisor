package pro.qyoga.core.therapy.programs.model

import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Table
import pro.qyoga.core.therapy.exercises.model.Exercise
import javax.annotation.concurrent.Immutable


// см. https://github.com/d-r-q/QYoga/issues/143
@Immutable
@Table("program_exercises")
class ProgramExercise(
    val exerciseRef: AggregateReference<Exercise, Long>
) {

    override fun toString(): String {
        return "ProgramExercise(exerciseRef=$exerciseRef)"
    }

}