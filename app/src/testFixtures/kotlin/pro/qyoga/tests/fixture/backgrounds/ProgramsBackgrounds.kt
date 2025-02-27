package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.qyoga.core.therapy.exercises.model.ExerciseRef
import pro.qyoga.core.therapy.programs.ProgramsRepo
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.backgrounds.exercises.ExerciseBackgrounds
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.ImagesGenerationMode
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.None
import pro.qyoga.tests.fixture.object_mothers.therapy.programs.ProgramsObjectMother

@Component
class ProgramsBackgrounds(
    private val programsRepo: ProgramsRepo,
    private val therapeuticTasksBackgrounds: TherapeuticTasksBackgrounds,
    private val exercisesBackgrounds: ExerciseBackgrounds
) {

    fun findAll(): Iterable<Program> {
        return programsRepo.findAll(fetch = Program.Fetch.therapistOnly)
    }

    fun createPrograms(
        count: Int,
        task: TherapeuticTask = therapeuticTasksBackgrounds.createTherapeuticTask()
    ): Iterable<Program> {
        val programs = (1..count).map { ProgramsObjectMother.randomProgram(therapeuticTask = task.ref()) }
        return programsRepo.saveAll(programs)
    }

    fun createRandomProgram(
        title: String = randomCyrillicWord(),
        exercisesCount: Int = 1,
        stepsInEachExercise: Int = 0,
        imagesGenerationMode: ImagesGenerationMode = None,
        therapistRef: TherapistRef = THE_THERAPIST_REF
    ): Program {
        val task = therapeuticTasksBackgrounds.createTherapeuticTask2(therapistRef)
        val exercises = exercisesBackgrounds.createExercises(
            exercisesCount,
            stepsInEachExercise,
            imagesGenerationMode,
            therapistRef
        )
        return programsRepo.save(
            ProgramsObjectMother.randomProgram(
                title,
                task.ref(),
                exercises = exercises.map { it.ref() },
                therapistRef
            )
        )
    }

    fun fetchExerciseImages(program: Program): List<ByteArray> {
        val imageKeys = program.exercises.map { it.exerciseRef.resolveOrThrow() }
            .flatMap { ex -> (0 until ex.steps.size).map { stepIdx -> ex.id to stepIdx } }

        val images =
            imageKeys.mapNotNull { (exId, stepIdx) -> exercisesBackgrounds.getExerciseStepImage(exId, stepIdx) }

        return images
    }

    fun createProgram(
        therapistRef: TherapistRef,
        taskRef: TherapeuticTaskRef,
        exercisesRefs: List<ExerciseRef>
    ): Program {
        return programsRepo.save(
            ProgramsObjectMother.randomProgram(
                therapeuticTask = taskRef,
                exercises = exercisesRefs,
                owner = therapistRef
            )
        )
    }

}
