package pro.qyoga.assertions

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import jakarta.mail.internet.MimeMessage
import pro.qyoga.core.users.api.RegisterTherapistRequest

val newRegistrationEmailPattern = ".*Имя: (.*), Email: (.*), пароль: (.*)\\..*".toRegex()

infix fun MimeMessage.shouldMatch(registerTherapistRequest: RegisterTherapistRequest): Pair<String, String> {
    (this.content as String) shouldMatch newRegistrationEmailPattern
    val emailMatcher = newRegistrationEmailPattern.matchEntire(this.content.toString())!!
    val name = emailMatcher.groupValues[1]
    name shouldBe registerTherapistRequest.fullName
    val receivedEmail = emailMatcher.groupValues[2]
    receivedEmail shouldBe registerTherapistRequest.email
    val password = emailMatcher.groupValues[3]
    return receivedEmail to password
}