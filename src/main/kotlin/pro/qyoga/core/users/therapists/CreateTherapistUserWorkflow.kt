package pro.qyoga.core.users.therapists

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.core.users.auth.UsersFactory
import pro.qyoga.core.users.auth.UsersRepo
import pro.qyoga.core.users.auth.model.Role


@Component
class CreateTherapistUserWorkflow(
    private val usersRepo: UsersRepo,
    private val therapistsRepo: TherapistsRepo,
    private val usersFactory: UsersFactory
) : (RegisterTherapistRequest, CharSequence) -> Therapist? {

    @Transactional
    override fun invoke(
        registerTherapistRequest: RegisterTherapistRequest,
        password: CharSequence
    ): Therapist? {
        var user = usersFactory.createUser(registerTherapistRequest.email, password, setOf(Role.ROLE_THERAPIST))
        user = usersRepo.save(user)
            ?: return null

        var therapist = Therapist(registerTherapistRequest.firstName, registerTherapistRequest.lastName, user.id)
        therapist = therapistsRepo.save(therapist)

        return therapist
    }

}
