package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import pro.qyoga.core.users.therapists.CreateTherapistUserWorkflow
import pro.qyoga.core.users.therapists.Therapist
import pro.qyoga.tests.fixture.data.randomPassword
import pro.qyoga.tests.fixture.object_mothers.therapists.TherapistsObjectMother

@Component
class UsersBackgrounds(
    private val createTherapistUser: CreateTherapistUserWorkflow
) {

    fun registerNewTherapist(): Therapist {
        return createTherapistUser(TherapistsObjectMother.registerTherapistRequest(), randomPassword())
    }

}