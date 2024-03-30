package pro.qyoga.tests.assertions

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import jakarta.mail.internet.MimeMessage
import pro.qyoga.core.users.therapists.RegisterTherapistRequest

val passwordEmailPattern = ("[\\s\\S]*Вы зарегистрировались в Trainer Advisor.[\\s\\S]*" +
        "Логин от вашего аккаунта: (.+)\\.[\\s\\S]*" +
        "Пароль от вашего аккаунта: (.+)\\.[\\s\\S]*").toRegex()

val newRegistrationNotificationEmailPattern = "Email: (.*)".toRegex()

infix fun MimeMessage.shouldBePasswordEmailFor(registerTherapistRequest: RegisterTherapistRequest) {
    (this.content as String) shouldMatch passwordEmailPattern
    val emailTextMatcher = passwordEmailPattern.matchEntire(this.content.toString())!!
    val userEmail = emailTextMatcher.groupValues[1]
    userEmail shouldBe registerTherapistRequest.email
}


infix fun MimeMessage.shouldMatch(registerTherapistRequest: RegisterTherapistRequest) {
    (this.content as String) shouldMatch newRegistrationNotificationEmailPattern
    val emailMatcher = newRegistrationNotificationEmailPattern.matchEntire(this.content.toString())!!
    val receivedEmail = emailMatcher.groupValues[1]
    receivedEmail shouldBe registerTherapistRequest.email
}