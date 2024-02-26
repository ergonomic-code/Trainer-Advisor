package pro.qyoga.tests.fixture.object_mothers.therapy.programs

import org.springframework.data.jdbc.core.mapping.AggregateReference
import pro.azhidkov.platform.file_storage.api.StoredFile
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.qyoga.core.therapy.exercises.model.Exercise
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.core.therapy.programs.model.*
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import pro.qyoga.core.users.therapists.Therapist
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
        exercises: Iterable<Exercise> = emptyList(),
        owner: AggregateReference<Therapist, Long> = THE_THERAPIST_REF,
    ): Program {
        return Program(
            title,
            therapeuticTask,
            owner,
            exercises.map { ProgramExercise(it.ref()) }
        )
    }

    fun docxProgram(
        program: Program,
        exercisesWithImages: List<Pair<Exercise, Map<Int, StoredFile>>>
    ): DocxProgram {
        return DocxProgram(
            program.id, program.title, exercisesWithImages.map { exerciseWithImages ->
                var idx: Int = 0
                docxExercise(exerciseWithImages.first, idx++, exerciseWithImages.second[idx]!!)
            }
        )
    }

    fun docxExercise(
        exercise: Exercise,
        idx: Int,
        storedFile: StoredFile
    ): DocxExercise {
        return DocxExercise(
            exercise.id,
            exercise.title,
            exercise.description,
            exercise.steps.map {
                DocxStep(idx, it.description, storedFile.id)
            })
    }
}