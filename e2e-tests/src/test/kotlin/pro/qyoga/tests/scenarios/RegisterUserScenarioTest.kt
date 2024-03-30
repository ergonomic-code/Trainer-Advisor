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
import pro.qyoga.tests.assertions.passwordEmailPattern
import pro.qyoga.tests.assertions.shouldBePasswordEmailFor
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.data.randomEmail
import pro.qyoga.tests.fixture.object_mothers.therapists.TherapistsObjectMother.registerTherapistRequest
import pro.qyoga.tests.infra.QYogaE2EBaseTest
import pro.qyoga.tests.pages.publc.LoginPage
import pro.qyoga.tests.pages.publc.RegisterPage
import pro.qyoga.tests.platform.selenide.`$`
import pro.qyoga.tests.platform.selenide.fastType


class RegisterUserScenarioTest : QYogaE2EBaseTest() {

    @Test
    fun `Register user scenario`() {
        // Given
        val registerTherapistRequest = registerTherapistRequest(
            randomCyrillicWord(),
            randomCyrillicWord(),
            randomEmail()
        )

        open("$baseUri${RegisterPage.path}")
        title() shouldBe ("Заявка на регистрацию")

        `$`(RegisterPage.RegisterForm.firstName).fastType(registerTherapistRequest.firstName)
        `$`(RegisterPage.RegisterForm.lastName).fastType(registerTherapistRequest.lastName)
        `$`(RegisterPage.RegisterForm.email).fastType(registerTherapistRequest.email)
        `$`(RegisterPage.RegisterForm.submit).click()

        `$`("div#registrationSuccess").should(Visible())

        val receivedMessages = greenMail.getReceivedMessagesForDomain(registerTherapistRequest.email)
        receivedMessages shouldHaveSize 1
        receivedMessages[0] shouldBePasswordEmailFor registerTherapistRequest
        val password = passwordEmailPattern.matchEntire(receivedMessages[0].content as String)!!.groupValues[2]

        open("$baseUri${LoginPage.path}")
        `$`(LoginPage.LoginForm.username).fastType(registerTherapistRequest.email)
        `$`(LoginPage.LoginForm.password).fastType(password)
        `$`(LoginPage.LoginForm.submit).click()
        title() shouldBe "Расписание"
    }

    companion object {

        @JvmField
        @RegisterExtension
        var greenMail: GreenMailExtension = GreenMailExtension(ServerSetupTest.SMTP.port(10_025))
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("qyogapro@yandex.ru", "password"))

    }

}