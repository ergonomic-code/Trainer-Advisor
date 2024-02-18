package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.qyoga.core.therapy.programs.impl.ProgramsRepo
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.tests.fixture.backgrounds.exercises.ExerciseBackgrounds
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.None
import pro.qyoga.tests.fixture.data.randomCyrillicWord
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
        stepsInEachExercise: Int = 0
    ): Program {
        val task = therapeuticTasksBackgrounds.createTherapeuticTask()
        val exercises = exercisesBackgrounds.createExercises(exercisesCount, stepsInEachExercise, None)
        return programsRepo.save(ProgramsObjectMother.randomProgram(title, task.ref(), exercises = exercises))
    }

}
