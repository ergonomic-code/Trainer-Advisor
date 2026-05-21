package pro.qyoga.core.users.therapists

import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import pro.azhidkov.platform.secrets.SecretChars
import pro.qyoga.core.users.auth.UsersRepo
import pro.qyoga.core.users.auth.model.Role
import pro.qyoga.core.users.auth.model.User

@Component
class CreateTherapistUserOp(
    private val usersRepo: UsersRepo,
    private val therapistsRepo: TherapistsRepo,
    private val passwordEncoder: PasswordEncoder
) {
    private val log = LoggerFactory.getLogger("createTherapistUser")

    operator fun invoke(
        registerTherapistRequest: RegisterTherapistRequest,
        password: SecretChars
    ): Therapist {
        log.info("Creating new therapist user for {}", registerTherapistRequest.email)

        var user = createUser(registerTherapistRequest.email, password, setOf(Role.ROLE_THERAPIST))
        user = usersRepo.save(user)

        var therapist = Therapist(registerTherapistRequest.firstName, registerTherapistRequest.lastName, user.id)
        therapist = therapistsRepo.save(therapist)

        log.info("Therapist user created, id = {}", user.id)

        return therapist
    }

    private fun createUser(email: String, plainPassword: SecretChars, roles: Set<Role>): User {
        val passwordHash = passwordEncoder.encode(plainPassword.show())!!
        return User(email, passwordHash, roles.toTypedArray(), enabled = true)
    }

}
