package pro.qyoga.app.publc

import org.springframework.stereotype.Component
import pro.qyoga.core.users.api.RegisterTherapistRequest
import pro.qyoga.core.users.api.Therapist
import pro.qyoga.core.users.api.UsersService
import pro.qyoga.infra.email.api.QyogaEmailsService
import pro.qyoga.infra.email.api.RegisteredUserNotification
import kotlin.random.Random


@Component
class RegisterTherapistWorkflow(
    private val usersService: UsersService,
    private val qyogaEmailsService: QyogaEmailsService
) {

    fun registerNewTherapist(registerTherapistRequest: RegisterTherapistRequest): Therapist? {
        val password = randomPassword()
        val therapist = usersService.registerNewTherapist(registerTherapistRequest, password)
            ?: return null

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
