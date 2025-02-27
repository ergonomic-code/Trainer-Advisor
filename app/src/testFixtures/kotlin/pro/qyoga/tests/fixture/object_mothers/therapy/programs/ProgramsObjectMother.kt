package pro.qyoga.tests.fixture.object_mothers.therapy.programs

import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.qyoga.core.therapy.exercises.model.Exercise
import pro.qyoga.core.therapy.exercises.model.ExerciseRef
import pro.qyoga.core.therapy.exercises.model.ExerciseStep
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.core.therapy.programs.dtos.ProgramsSearchFilter
import pro.qyoga.core.therapy.programs.model.DocxExercise
import pro.qyoga.core.therapy.programs.model.DocxProgram
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.core.therapy.programs.model.ProgramExercise
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF


object ProgramsObjectMother {

    fun randomCreateProgramRequest(
        title: String = randomCyrillicWord(),
        exercises: Iterable<Exercise> = emptyList()
    ) = CreateProgramRequest(title, exercises.map { it.id })

    fun randomProgram(
        title: String = randomCyrillicWord(),
        therapeuticTask: TherapeuticTaskRef,
        exercises: Iterable<ExerciseRef> = emptyList(),
        owner: TherapistRef = THE_THERAPIST_REF,
    ): Program {
        return Program(
            title,
            therapeuticTask,
            owner,
            exercises.map { ProgramExercise(it) }
        )
    }

    fun docxProgram(
        program: Program
    ): DocxProgram {
        return DocxProgram(
            program.id,
            program.title,
            program.exercises.map { docxExercise(it.exerciseRef.resolveOrThrow()) }
        )
    }

    private fun docxExercise(
        exercise: Exercise
    ): DocxExercise {
        return DocxExercise(
            exercise.id,
            exercise.title,
            exercise.description,
            exercise.steps.map { ExerciseStep(it.description, it.imageId) })
    }

    fun aProgramSearchFilter(
        titleKeyword: String = "",
        therapeuticTaskKeyword: String = ""
    ) = ProgramsSearchFilter(
        titleKeyword = titleKeyword,
        therapeuticTaskKeyword = therapeuticTaskKeyword
    )
}