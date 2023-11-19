package pro.qyoga.infra.email.internal

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import pro.qyoga.infra.email.api.QyogaEmailsService
import pro.qyoga.infra.email.api.RegisteredUserNotification


@Service
class QyogaEmailsServiceImpl(
    private val mailSender: JavaMailSender
) : QyogaEmailsService {

    override fun sendNewRegistrationNotification(registeredUserNotification: RegisteredUserNotification) {
        val mail = mailSender.createMimeMessage()
        MimeMessageHelper(mail).apply {
            setFrom("qyogapro@yandex.ru")
            setTo("me@azhidkov.pro")
            setSubject("Новая регистрация в QYoga!")
            setText(
                "Имя: ${registeredUserNotification.name}, " +
                        "Email: ${registeredUserNotification.email}, " +
                        "пароль: ${registeredUserNotification.password}."
            )
        }
        mailSender.send(mail)
    }

}