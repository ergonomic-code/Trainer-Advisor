package pro.qyoga.tests.cases.app.therapist.appointments.core

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.therapist.appointments.CreateAppointmentPage
import pro.qyoga.tests.clients.pages.therapist.appointments.SchedulePage
import pro.qyoga.tests.fixture.appointments.AppointmentsObjectMother
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class CreateAppointmentPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Create training session page should be rendered correctly`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointments.getCreateAppointmentPage()

        // Then
        document shouldBePage CreateAppointmentPage
    }

    @Test
    fun `Creation of appointment with required-only fields should be persistent`() {
        // Given
        val client = backgrounds.clients.createClients(1).single()
        val therapeuticTask = backgrounds.therapeuticTasks.createTherapeuticTask(1)
        val editAppointmentRequest = AppointmentsObjectMother.randomEditAppointmentRequest(
            client = client.ref(),
            therapeuticTask = therapeuticTask.ref()
        )

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val response = therapist.appointments.createAppointment(editAppointmentRequest)

        // Then
        response.statusCode() shouldBe HttpStatus.OK.value()
        response.header("HX-Redirect") shouldBe SchedulePage.PATH

        val storedAppointment = backgrounds.appointments.findAll().single()
        storedAppointment shouldMatch editAppointmentRequest
    }

    @Test
    fun `Creation of appointment with all fields should be persistent`() {
        // Given
        val client = backgrounds.clients.createClients(1).single()
        val therapeuticTask = backgrounds.therapeuticTasks.createTherapeuticTask(1)
        val editAppointmentRequest = AppointmentsObjectMother.randomFullEditAppointmentRequest(
            client = client.ref(),
            therapeuticTask = therapeuticTask.ref()
        )

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val response = therapist.appointments.createAppointment(editAppointmentRequest)

        // Then
        response.statusCode() shouldBe HttpStatus.OK.value()
        response.header("HX-Redirect") shouldBe SchedulePage.PATH

        val storedAppointment = backgrounds.appointments.findAll().single()
        storedAppointment shouldMatch editAppointmentRequest
    }

}