package pro.qyoga.infra.email.api


interface QyogaEmailsService {

    fun sendNewRegistrationNotification(registeredUserNotification: RegisteredUserNotification)

}