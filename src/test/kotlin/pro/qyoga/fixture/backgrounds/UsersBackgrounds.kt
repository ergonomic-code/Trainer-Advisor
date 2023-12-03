package pro.qyoga.fixture.backgrounds

import org.springframework.stereotype.Component
import pro.qyoga.core.users.api.Therapist
import pro.qyoga.core.users.api.UsersService
import pro.qyoga.fixture.data.randomPassword
import pro.qyoga.fixture.therapists.TherapistsObjectMother

@Component
class UsersBackgrounds(
    private val usersService: UsersService
) {

    fun registerNewTherapist(): Therapist {
        return usersService.registerNewTherapist(TherapistsObjectMother.registerTherapistRequest(), randomPassword())!!
    }

}