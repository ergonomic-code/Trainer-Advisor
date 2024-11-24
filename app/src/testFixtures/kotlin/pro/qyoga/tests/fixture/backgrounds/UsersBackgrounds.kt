package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import pro.qyoga.core.users.auth.UsersFactory
import pro.qyoga.core.users.auth.UsersRepo
import pro.qyoga.core.users.auth.impl.TaUserDetailsService
import pro.qyoga.core.users.therapists.Therapist
import pro.qyoga.core.users.therapists.TherapistsRepo
import pro.qyoga.core.users.therapists.createTherapistUser
import pro.qyoga.tests.fixture.data.randomPassword
import pro.qyoga.tests.fixture.object_mothers.therapists.TherapistsObjectMother.registerTherapistRequest

@Component
class UsersBackgrounds(
    private val usersRepo: UsersRepo,
    private val therapistsRepo: TherapistsRepo,
    private val usersFactory: UsersFactory,
    private val userDetailsService: TaUserDetailsService
) {

    fun registerNewTherapist(): Therapist {
        return createTherapistUser(
            usersRepo,
            therapistsRepo,
            usersFactory,
            registerTherapistRequest(),
            randomPassword()
        )
    }

    fun disable(userLogin: String) {
        userDetailsService.disableUser(userLogin)
    }

}