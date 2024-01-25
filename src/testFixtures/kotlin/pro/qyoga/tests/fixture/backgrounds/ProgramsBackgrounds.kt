package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import pro.qyoga.core.therapy.programs.ProgramsRepo
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.platform.spring.sdj.erpo.hydration.ref
import pro.qyoga.tests.fixture.backgrounds.exercises.ExerciseBackgrounds
import pro.qyoga.tests.fixture.backgrounds.exercises.None
import pro.qyoga.tests.fixture.therapy.programs.ProgramsObjectMother

@Component
class ProgramsBackgrounds(
    private val programsRepo: ProgramsRepo,
    private val therapeuticTasksBackgrounds: TherapeuticTasksBackgrounds,
    private val exercisesBackgrounds: ExerciseBackgrounds
) {

    fun findAll(): Iterable<Program> {
        return programsRepo.findAll(fetch = Program.Fetch.therapistOnly)
    }

    fun createPrograms(count: Int): Iterable<Program> {
        val task = therapeuticTasksBackgrounds.createTherapeuticTask()
        val programs = (1..count).map { ProgramsObjectMother.randomProgram(task.ref()) }
        return programsRepo.saveAll(programs)
    }

    fun createRandomProgram(exercisesCount: Int, stepsInEachExercise: Int): Program {
        val task = therapeuticTasksBackgrounds.createTherapeuticTask()
        val exercises = exercisesBackgrounds.createExercises(exercisesCount, stepsInEachExercise, None)
        return programsRepo.save(ProgramsObjectMother.randomProgram(task.ref(), exercises = exercises))
    }

}
