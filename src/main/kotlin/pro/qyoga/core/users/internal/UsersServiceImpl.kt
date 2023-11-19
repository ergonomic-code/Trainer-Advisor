package pro.qyoga.core.users.internal

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.core.users.api.*
import pro.qyoga.infra.email.api.QyogaEmailsService
import pro.qyoga.infra.email.api.RegisteredUserNotification
import kotlin.random.Random


@Service
class UsersServiceImpl(
    private val usersRepo: UsersRepo,
    private val therapistsRepo: TherapistsRepo,
    private val passwordEncoder: PasswordEncoder,
    private val qyogaEmailsService: QyogaEmailsService
) : UsersService {

    @Transactional
    override fun registerNewTherapist(registerTherapistRequest: RegisterTherapistRequest): Therapist? {
        val password = randomPassword()
        val passwordHash = passwordEncoder.encode(password)

        var user = User(registerTherapistRequest.email, passwordHash, arrayOf(Role.ROLE_THERAPIST))
        user = usersRepo.save(user)
            ?: return null

        var therapist = Therapist(registerTherapistRequest.firstName, registerTherapistRequest.lastName, user.id)
        therapist = therapistsRepo.save(therapist)

        val registeredUserNotification =
            RegisteredUserNotification(registerTherapistRequest.fullName, registerTherapistRequest.email, password)
        qyogaEmailsService.sendNewRegistrationNotification(registeredUserNotification)

        return therapist
    }

}

private val passwordChars = ('a'..'z').toList() + ('A'..'Z').toList() + ('0'..'9').toList()

private const val PASSWORD_LENGTH = 8

private fun randomPassword() =
    buildString {
        repeat(PASSWORD_LENGTH) {
            append(passwordChars[Random.nextInt(passwordChars.size)])
        }
    }
