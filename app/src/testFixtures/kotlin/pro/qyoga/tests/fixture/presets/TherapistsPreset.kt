package pro.qyoga.tests.fixture.presets

import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.tests.fixture.backgrounds.UsersBackgrounds
import pro.qyoga.tests.fixture.backgrounds.exercises.ExerciseBackgrounds


@Component
class TherapistsPreset(
    private val usersBackgrounds: UsersBackgrounds,
    private val exerciseBackgrounds: ExerciseBackgrounds
) {

    fun createTherapistWithExercise() {
        val therapist = usersBackgrounds.registerNewTherapist()
        exerciseBackgrounds.createExercise(ownerRef = therapist.ref())
    }

}