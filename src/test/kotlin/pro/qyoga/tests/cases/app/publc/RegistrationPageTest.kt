package pro.qyoga.tests.cases.app.publc

import com.icegreen.greenmail.configuration.GreenMailConfiguration
import com.icegreen.greenmail.junit5.GreenMailExtension
import com.icegreen.greenmail.util.ServerSetupTest
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.PublicClient
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.pages.publc.RegisterPage
import pro.qyoga.tests.pages.publc.RegistrationSuccessFragment
import pro.qyoga.tests.pages.therapist.clients.ClientsListPage
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_LOGIN
import pro.qyoga.tests.fixture.object_mothers.therapists.TherapistsObjectMother.registerTherapistRequest
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class RegistrationPageTest : QYogaAppIntegrationBaseTest() {

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
        document shouldHaveComponent RegistrationSuccessFragment

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
        document.select(RegisterPage.RegisterForm.DUPLICATED_EMAIL_MESSAGE)[0].text() shouldContain adminEmail
    }

    companion object {

        @JvmField
        @RegisterExtension
        var greenMail: GreenMailExtension = GreenMailExtension(ServerSetupTest.SMTP.port(10_025))
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("qyogapro@yandex.ru", "password"))

    }

}