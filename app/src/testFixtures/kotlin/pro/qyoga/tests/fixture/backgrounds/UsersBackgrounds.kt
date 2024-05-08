package pro.qyoga.tests.fixture.backgrounds

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.auth.impl.TaUserDetailsService
import pro.qyoga.core.users.therapists.CreateTherapistUserWorkflow
import pro.qyoga.core.users.therapists.Therapist
import pro.qyoga.core.users.therapists.TherapistsRepo
import pro.qyoga.tests.fixture.data.randomPassword
import pro.qyoga.tests.fixture.object_mothers.therapists.TherapistsObjectMother

@Component
class UsersBackgrounds(
    private val createTherapistUser: CreateTherapistUserWorkflow,
    private val userDetailsService: TaUserDetailsService,
    private val therapistsRepo: TherapistsRepo,
) {

    fun registerNewTherapist(): Therapist {
        return createTherapistUser(TherapistsObjectMother.registerTherapistRequest(), randomPassword())
    }

    fun disable(userLogin: String) {
        userDetailsService.disableUser(userLogin)
    }

    fun findTherapist(email: String): Therapist? {
        val id = (userDetailsService.loadUserByUsername(email) as? QyogaUserDetails)?.id
            ?: return null

        return therapistsRepo.findByIdOrNull(id)
    }

}