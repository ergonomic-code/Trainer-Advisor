package pro.qyoga.tests.fixture.presets

import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.core.therapy.exercises.model.Exercise
import pro.qyoga.core.users.therapists.Therapist
import pro.qyoga.tests.fixture.backgrounds.ProgramsBackgrounds
import pro.qyoga.tests.fixture.backgrounds.TherapeuticTasksBackgrounds
import pro.qyoga.tests.fixture.backgrounds.UsersBackgrounds
import pro.qyoga.tests.fixture.backgrounds.exercises.ExerciseBackgrounds


@Component
class TherapistsFixturePreset(
    private val usersBackgrounds: UsersBackgrounds,
    private val exerciseBackgrounds: ExerciseBackgrounds,
    private val programsBackgrounds: ProgramsBackgrounds,
    private val therapeuticTasksBackgrounds: TherapeuticTasksBackgrounds
) {

    fun createTherapistWithExercise(): Pair<Therapist, Exercise> {
        val therapist = usersBackgrounds.registerNewTherapist()
        val exercise = exerciseBackgrounds.createExercise(ownerRef = therapist.ref())
        return therapist to exercise
    }

    fun createTherapistWithProgram() {
        val (therapist, exercise) = createTherapistWithExercise()
        val task = therapeuticTasksBackgrounds.createTherapeuticTask2(therapist.ref())
        programsBackgrounds.createProgram(
            therapistRef = therapist.ref(),
            taskRef = task.ref(),
            exercisesRefs = listOf(exercise.ref())
        )
    }

}