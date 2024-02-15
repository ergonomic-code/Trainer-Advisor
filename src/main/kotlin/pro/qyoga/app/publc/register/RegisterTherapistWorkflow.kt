package pro.qyoga.app.publc.register

import org.springframework.stereotype.Component
import pro.qyoga.core.users.therapists.CreateTherapistUserWorkflow
import pro.qyoga.core.users.therapists.RegisterTherapistRequest
import pro.qyoga.core.users.therapists.Therapist
import pro.qyoga.infra.email.api.QyogaEmailsService
import pro.qyoga.infra.email.api.RegisteredUserNotification
import kotlin.random.Random

@Component
class RegisterTherapistWorkflow(
    private val createTherapistUser: CreateTherapistUserWorkflow,
    private val qyogaEmailsService: QyogaEmailsService
) : (RegisterTherapistRequest) -> Therapist {

    override fun invoke(registerTherapistRequest: RegisterTherapistRequest): Therapist {
        val password = randomPassword()
        val therapist = createTherapistUser(registerTherapistRequest, password)

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
