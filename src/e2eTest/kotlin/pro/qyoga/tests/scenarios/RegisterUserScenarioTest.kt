package pro.qyoga.tests.scenarios

import com.codeborne.selenide.Selenide.*
import com.codeborne.selenide.conditions.Visible
import com.icegreen.greenmail.configuration.GreenMailConfiguration
import com.icegreen.greenmail.junit5.GreenMailExtension
import com.icegreen.greenmail.util.ServerSetupTest
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.data.randomEmail
import pro.qyoga.tests.fixture.therapists.TherapistsObjectMother.registerTherapistRequest
import pro.qyoga.tests.infra.QYogaE2EBaseTest


class RegisterUserScenarioTest : QYogaE2EBaseTest() {

    private val adminEmail = getBean<Environment>()["qyoga.admin.email"]

    @Test
    fun `Register user scenario`() {
        // Given
        val registerTherapistRequest = registerTherapistRequest(
            randomCyrillicWord(),
            randomCyrillicWord(),
            randomEmail()
        )

        open("$baseUri/register")
        title() shouldBe ("Заявка на регистрацию")

        `$`("input[name=firstName]").type(registerTherapistRequest.firstName)
        `$`("input[name=lastName]").type(registerTherapistRequest.lastName)
        `$`("input[name=email]").type(registerTherapistRequest.email)
        `$`("input[name=register]").click()

        `$`("div#registrationSuccess").should(Visible())

        val receivedMessages = greenMail.getReceivedMessagesForDomain(adminEmail)
        receivedMessages shouldHaveSize 1
        val (receivedEmail, password) = receivedMessages[0] shouldMatch registerTherapistRequest

        open("$baseUri/login")
        `$`("input[name=username]").type(receivedEmail)
        `$`("input[name=password]").type(password)
        `$`("#submit").click()

        title() shouldBe "Расписание"
    }

    companion object {

        @JvmField
        @RegisterExtension
        var greenMail: GreenMailExtension = GreenMailExtension(ServerSetupTest.SMTP.port(10_025))
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("qyogapro@yandex.ru", "password"))

    }

}