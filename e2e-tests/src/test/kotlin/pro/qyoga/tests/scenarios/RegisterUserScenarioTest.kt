package pro.qyoga.tests.scenarios

import com.codeborne.selenide.Selenide.*
import com.codeborne.selenide.conditions.Visible
import com.icegreen.greenmail.configuration.GreenMailConfiguration
import com.icegreen.greenmail.junit5.GreenMailExtension
import com.icegreen.greenmail.util.ServerSetupTest
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
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
import pro.qyoga.tests.pages.therapist.appointments.CalendarPage
import pro.qyoga.tests.platform.selenide.`$`
import pro.qyoga.tests.platform.selenide.await
import pro.qyoga.tests.platform.selenide.click
import pro.qyoga.tests.platform.selenide.typeInto
import java.util.*


@DisplayName("Регистрация терапевта")
class RegisterUserScenarioTest : QYogaE2EBaseTest() {

    @DisplayName("Успешная регистрация")
    @Test
    fun successFullUserRegistration() {
        // Фикстура
        val registerTherapistRequest = registerTherapistRequest(
            randomCyrillicWord(),
            randomCyrillicWord(),
            randomEmail()
        )

        // Пользователь переходит по урлу страницы регистрации
        open(RegisterPage.path)

        // И видит страницу регистрации
        title() shouldBe (RegisterPage.title)

        // Затем он заполяет и отправляет форму
        typeInto(RegisterPage.RegisterForm.firstName, registerTherapistRequest.firstName)
        typeInto(RegisterPage.RegisterForm.lastName, registerTherapistRequest.lastName)
        typeInto(RegisterPage.RegisterForm.email, registerTherapistRequest.email)
        typeInto(
            RegisterPage.RegisterForm.captchaCode,
            backgrounds.captchaBackgrounds.getCaptchaCode(UUID.fromString(`$`(RegisterPage.RegisterForm.captchaId).value!!))
        )

        click(RegisterPage.RegisterForm.submit)

        // И видит сообщение об успешной регистрации
        `$`("div#registrationSuccess").should(Visible())

        // И получает письмо с паролем
        val receivedMessages = greenMail.getReceivedMessagesForDomain(registerTherapistRequest.email)
        receivedMessages shouldHaveSize 1
        receivedMessages[0] shouldBePasswordEmailFor registerTherapistRequest
        val password = passwordEmailPattern.matchEntire(receivedMessages[0].content as String)!!.groupValues[2]

        // Затем пользователь входит
        open(LoginPage.path)
        typeInto(LoginPage.LoginForm.username, registerTherapistRequest.email)
        typeInto(LoginPage.LoginForm.password, password)
        click(LoginPage.LoginForm.submit)

        // И видит страницу расписания
        await(CalendarPage)
        title() shouldBe CalendarPage.title
    }

    companion object {

        @JvmField
        @RegisterExtension
        var greenMail: GreenMailExtension = GreenMailExtension(ServerSetupTest.SMTP.port(10_025))
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("qyogapro@yandex.ru", "password"))

    }

}