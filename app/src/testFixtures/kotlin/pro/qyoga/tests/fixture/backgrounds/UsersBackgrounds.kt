package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import pro.qyoga.core.users.auth.impl.TaUserDetailsService
import pro.qyoga.core.users.therapists.CreateTherapistUserOp
import pro.qyoga.core.users.therapists.Therapist
import pro.qyoga.tests.fixture.data.randomPassword
import pro.qyoga.tests.fixture.object_mothers.therapists.TherapistsObjectMother.registerTherapistRequest

@Component
class UsersBackgrounds(
    private val createTherapistUser: CreateTherapistUserOp,
    private val userDetailsService: TaUserDetailsService
) {

    fun createNewTherapist(): Therapist {
        return createTherapistUser(
            registerTherapistRequest = registerTherapistRequest(),
            password = randomPassword()
        )
    }

    fun disable(userLogin: String) {
        userDetailsService.disableUser(userLogin)
    }

}
