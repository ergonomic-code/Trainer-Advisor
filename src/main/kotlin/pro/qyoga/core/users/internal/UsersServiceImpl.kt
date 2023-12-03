package pro.qyoga.core.users.internal

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.core.users.api.*


@Service
class UsersServiceImpl(
    private val usersRepo: UsersRepo,
    private val therapistsRepo: TherapistsRepo,
    private val passwordEncoder: PasswordEncoder
) : UsersService {

    @Transactional
    override fun registerNewTherapist(
        registerTherapistRequest: RegisterTherapistRequest,
        password: String
    ): Therapist? {
        val passwordHash = passwordEncoder.encode(password)

        var user = User(registerTherapistRequest.email, passwordHash, arrayOf(Role.ROLE_THERAPIST))
        user = usersRepo.save(user)
            ?: return null

        var therapist = Therapist(registerTherapistRequest.firstName, registerTherapistRequest.lastName, user.id)
        therapist = therapistsRepo.save(therapist)

        return therapist
    }

}
