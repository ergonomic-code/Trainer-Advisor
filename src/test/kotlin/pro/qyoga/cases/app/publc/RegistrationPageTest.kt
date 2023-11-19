package pro.qyoga.cases.app.publc

import com.icegreen.greenmail.configuration.GreenMailConfiguration
import com.icegreen.greenmail.junit5.GreenMailExtension
import com.icegreen.greenmail.util.ServerSetupTest
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import pro.qyoga.assertions.shouldBe
import pro.qyoga.assertions.shouldHave
import pro.qyoga.assertions.shouldMatch
import pro.qyoga.clients.PublicClient
import pro.qyoga.clients.TherapistClient
import pro.qyoga.clients.pages.publc.RegisterPage
import pro.qyoga.clients.pages.publc.RegistrationSuccessFragment
import pro.qyoga.clients.pages.therapist.clients.ClientsListPage
import pro.qyoga.fixture.therapists.THE_THERAPIST_LOGIN
import pro.qyoga.fixture.therapists.TherapistsObjectMother.registerTherapistRequest
import pro.qyoga.infra.web.QYogaAppBaseTest


class RegistrationPageTest : QYogaAppBaseTest() {

    val adminEmail = getBean<Environment>()["qyoga.admin.email"]!!

    @Test
    fun `Registration page should be rendered correctly`() {
        // Given

        // When
        val document = PublicClient.authApi.getRegistrationPage()

        // Then
        document shouldBe RegisterPage
    }

    @Test
    fun `After submit of registration form therapist should be created and creds should be sent to admin and success response should be returned`() {
        // Given
        val registerTherapistRequest = registerTherapistRequest(
            "Сергей",
            "Сергеев",
            "new-therapist@qyoga.pro"
        )

        // When
        val document = PublicClient.authApi.registerTherapist(registerTherapistRequest)

        // Then
        document shouldBe RegistrationSuccessFragment

        // And then
        val receivedMessages = greenMail.getReceivedMessagesForDomain(adminEmail)
        receivedMessages shouldHaveSize 1
        val (receivedTherapistEmail, password) = receivedMessages[0] shouldMatch registerTherapistRequest

        // And when
        val therapist = TherapistClient.login(receivedTherapistEmail, password)
        val getClientsResponse = therapist.clients.getClientsListPage()

        // Then
        getClientsResponse shouldBe ClientsListPage
    }

    @Test
    fun `When user enters existing email, error message should be returned`() {
        // Given
        val registerTherapistRequest = registerTherapistRequest(email = THE_THERAPIST_LOGIN)

        // When
        val document = PublicClient.authApi.registerTherapist(registerTherapistRequest)

        // Then
        document shouldHave RegisterPage.RegisterForm.duplicatedEmail
        document.select(RegisterPage.RegisterForm.duplicatedEmailMessage)[0].text() shouldContain adminEmail
    }

    companion object {

        @JvmField
        @RegisterExtension
        var greenMail: GreenMailExtension = GreenMailExtension(ServerSetupTest.SMTP.port(10_025))
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("qyogapro@yandex.ru", "password"))

    }

}