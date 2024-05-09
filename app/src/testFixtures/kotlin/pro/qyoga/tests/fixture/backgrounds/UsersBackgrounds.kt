package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import pro.qyoga.core.users.auth.impl.TaUserDetailsService
import pro.qyoga.core.users.therapists.CreateTherapistUserWorkflow
import pro.qyoga.core.users.therapists.Therapist
import pro.qyoga.tests.fixture.data.randomPassword
import pro.qyoga.tests.fixture.object_mothers.therapists.TherapistsObjectMother

@Component
class UsersBackgrounds(
    private val createTherapistUser: CreateTherapistUserWorkflow,
    private val userDetailsService: TaUserDetailsService
) {

    fun registerNewTherapist(): Therapist {
        return createTherapistUser(TherapistsObjectMother.registerTherapistRequest(), randomPassword())
    }

    fun disable(userLogin: String) {
        userDetailsService.disableUser(userLogin)
    }

}