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
import pro.qyoga.tests.assertions.*
import pro.qyoga.tests.clients.PublicClient
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_LOGIN
import pro.qyoga.tests.fixture.object_mothers.therapists.TherapistsObjectMother.registerTherapistRequest
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.publc.RegisterPage
import pro.qyoga.tests.pages.publc.RegistrationSuccessFragment
import pro.qyoga.tests.pages.therapist.clients.ClientsListPage


class RegistrationPageTest : QYogaAppIntegrationBaseTest() {

    val adminEmail = getBean<Environment>()["trainer-advisor.admin.email"]!!

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
        val userEmail = "new-therapist@trainer-advisor.pro"
        val registerTherapistRequest = registerTherapistRequest(
            "Сергей",
            "Сергеев",
            userEmail
        )

        // When
        val document = PublicClient.authApi.registerTherapist(registerTherapistRequest)

        // Then
        document shouldHaveComponent RegistrationSuccessFragment

        // And then
        val userReceivedMessages = greenMail.getReceivedMessagesForDomain(userEmail)
        userReceivedMessages shouldHaveSize 1
        userReceivedMessages[0] shouldBePasswordEmailFor registerTherapistRequest
        val password = passwordEmailPattern.matchEntire(userReceivedMessages[0].content as String)!!.groupValues[2]

        // And then
        val adminReceivedMessages = greenMail.getReceivedMessagesForDomain(adminEmail)
        adminReceivedMessages shouldHaveSize 1
        adminReceivedMessages[0] shouldMatch registerTherapistRequest

        // And when
        val therapist = TherapistClient.login(userEmail, password)
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