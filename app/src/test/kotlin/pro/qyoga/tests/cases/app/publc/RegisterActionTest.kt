package pro.qyoga.tests.cases.app.publc

import com.icegreen.greenmail.configuration.GreenMailConfiguration
import com.icegreen.greenmail.junit5.GreenMailExtension
import com.icegreen.greenmail.util.ServerSetupTest
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import pro.qyoga.tests.assertions.*
import pro.qyoga.tests.clients.PublicClient
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_LOGIN
import pro.qyoga.tests.fixture.object_mothers.therapists.TherapistsObjectMother.captchaAnswer
import pro.qyoga.tests.fixture.object_mothers.therapists.TherapistsObjectMother.registerTherapistRequest
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.publc.RegisterPage
import pro.qyoga.tests.pages.publc.RegistrationSuccessFragment
import pro.qyoga.tests.pages.therapist.clients.ClientsListPage


@DisplayName("Метод регистрации пользователя")
class RegisterActionTest : QYogaAppIntegrationBaseTest() {

    private val adminEmail = getBean<Environment>()["trainer-advisor.admin.email"]!!

    @Test
    fun `должен создавать нового терапевта и отправлять письмо с паролем пользователю и письмо с оповещением админу`() {
        // Сетап
        val userEmail = "new-therapist@trainer-advisor.pro"
        val (captchaId, captchaCode) = backgrounds.captchaBackgrounds.generateCaptcha()
        val registerTherapistRequest = registerTherapistRequest(
            "Сергей",
            "Сергеев",
            userEmail,
            captchaAnswer(captchaId, captchaCode)
        )

        // Действие №1
        val document = PublicClient.authApi.registerTherapist(registerTherapistRequest)

        // Проверка №1
        document shouldHaveComponent RegistrationSuccessFragment

        // Проверка №2
        val userReceivedMessages = greenMail.getReceivedMessagesForDomain(userEmail)
        userReceivedMessages shouldHaveSize 1
        userReceivedMessages[0] shouldBePasswordEmailFor registerTherapistRequest
        val password = passwordEmailPattern.matchEntire(userReceivedMessages[0].content as String)!!.groupValues[2]

        // Проверка №3
        val adminReceivedMessages = greenMail.getReceivedMessagesForDomain(adminEmail)
        adminReceivedMessages shouldHaveSize 1
        adminReceivedMessages[0] shouldMatch registerTherapistRequest

        // Действие №1
        val therapist = TherapistClient.login(userEmail, password)
        val getClientsResponse = therapist.clients.getClientsListPage()

        // Проверка
        getClientsResponse shouldBe ClientsListPage
    }

    @Test
    fun `должен возвращать соответствующую ошибку если пользователь ввёл существующий емейл`() {
        // Сетап
        val (captchaId, captchaCode) = backgrounds.captchaBackgrounds.generateCaptcha()
        val registerTherapistRequest = registerTherapistRequest(
            email = THE_THERAPIST_LOGIN,
            captchaAnswer = captchaAnswer(captchaId, captchaCode)
        )

        // Действие
        val document = PublicClient.authApi.registerTherapist(registerTherapistRequest)

        // Проверка
        document shouldHaveElement RegisterPage.RegisterForm.duplicatedEmail
        document.select(RegisterPage.RegisterForm.DUPLICATED_EMAIL_MESSAGE)[0].text() shouldContain adminEmail
    }

    @Test
    fun `должен возвращать соответствующую ошибку если пользователь ввёл некорректный код капчи`() {
        // Given
        val (captchaId) = backgrounds.captchaBackgrounds.generateCaptcha()
        val registerTherapistRequest = registerTherapistRequest(
            email = THE_THERAPIST_LOGIN,
            captchaAnswer = captchaAnswer(captchaId, faker.text().text(6))
        )

        // When
        val document = PublicClient.authApi.registerTherapist(registerTherapistRequest)

        // Then
        document shouldHaveElement RegisterPage.RegisterForm.invalidCaptcha
    }

    companion object {

        @JvmField
        @RegisterExtension
        var greenMail: GreenMailExtension = GreenMailExtension(ServerSetupTest.SMTP.port(10_025))
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("qyogapro@yandex.ru", "password"))

    }

}