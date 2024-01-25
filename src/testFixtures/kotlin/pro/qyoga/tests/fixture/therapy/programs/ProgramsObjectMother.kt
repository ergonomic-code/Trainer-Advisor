package pro.qyoga.tests.fixture.therapy.programs

import org.springframework.data.jdbc.core.mapping.AggregateReference
import pro.qyoga.core.therapy.exercises.model.Exercise
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.core.therapy.programs.model.ProgramExercise
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.core.users.api.Therapist
import pro.qyoga.platform.spring.sdj.erpo.hydration.ref
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_REF


object ProgramsObjectMother {

    fun randomProgram(
        therapeuticTask: AggregateReference<TherapeuticTask, Long>,
        exercises: Iterable<Exercise> = emptyList(),
        owner: AggregateReference<Therapist, Long> = THE_THERAPIST_REF,
    ): Program = Program(
        randomCyrillicWord(),
        therapeuticTask,
        owner,
        exercises.map { ProgramExercise(it.ref()) }
    )

}