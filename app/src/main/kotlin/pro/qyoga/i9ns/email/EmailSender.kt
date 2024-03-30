package pro.qyoga.i9ns.email

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
class EmailSender(
    private val mailSender: JavaMailSender
) {

    fun sendEmail(email: Email) {
        val mail = mailSender.createMimeMessage().apply {
            MimeMessageHelper(this).apply {
                setFrom(email.from)
                setTo(email.to)
                setSubject(email.subject)
                setText(email.text)
            }
        }
        mailSender.send(mail)
    }

}