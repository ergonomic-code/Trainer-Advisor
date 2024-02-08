package pro.qyoga.tests.cases.app.therapist.appointments.core

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.clients.pages.therapist.appointments.EditAppointmentForm
import pro.qyoga.tests.clients.pages.therapist.appointments.EditAppointmentPage
import pro.qyoga.tests.clients.pages.therapist.appointments.FutureSchedulePage
import pro.qyoga.tests.fixture.appointments.AppointmentsObjectMother
import pro.qyoga.tests.fixture.appointments.randomAppointmentCost
import pro.qyoga.tests.fixture.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.infra.html.HtmlPage
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class EditAppointmentPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Edit page for minimal appointment page should be correctly rendered and prefilled`() {
        // Given
        val appointment = backgrounds.appointments.create()

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointments.getEditAppointmentPage(appointment.id)

        // Then
        document shouldBePage EditAppointmentPage
        document shouldHave EditAppointmentForm.formPrefilledWith(appointment)
    }

    @Test
    fun `Edit page for full appointment page should be correctly rendered and prefilled`() {
        // Given
        val appointment = backgrounds.appointments.createFull()

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointments.getEditAppointmentPage(appointment.id)

        // Then
        document shouldBePage EditAppointmentPage
        document shouldHave EditAppointmentForm.formPrefilledWith(appointment)
    }

    @Test
    fun `Edit of minimal appointment to full should be persistent`() {
        // Given
        val appointment = backgrounds.appointments.create()
        val newAppointmentType = backgrounds.appointmentTypes.createAppointmentType()
        val therapeuticTask = backgrounds.therapeuticTasks.createTherapeuticTask()
        val editedAppointment =
            AppointmentsObjectMother.appointmentPatchRequest(
                appointment,
                newAppointmentType.ref(),
                newAppointmentType.name,
                therapeuticTask = therapeuticTask.ref(),
                cost = randomAppointmentCost(),
                payed = true,
                place = randomCyrillicWord(),
                comment = randomCyrillicWord()
            )

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val response = therapist.appointments.editAppointment(appointment.id, editedAppointment)

        // Then
        response.statusCode() shouldBe HttpStatus.OK.value()
        response shouldBePage FutureSchedulePage

        val persistedAppointment = backgrounds.appointments.findAll().single()
        persistedAppointment shouldMatch editedAppointment
    }

    @Test
    fun `Edit of full appointment to minimal should be persistent`() {
        // Given
        val appointment = backgrounds.appointments.createFull()
        val editedAppointment =
            AppointmentsObjectMother.appointmentPatchRequest(
                appointment,
                appointment.typeRef,
                appointment.typeRef.resolveOrThrow().name,
                therapeuticTask = null,
                cost = null,
                payed = false,
                place = null,
                comment = null
            )

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val response = therapist.appointments.editAppointment(appointment.id, editedAppointment)

        // Then
        response.statusCode() shouldBe HttpStatus.OK.value()
        response shouldBePage FutureSchedulePage

        val persistedAppointment = backgrounds.appointments.findAll().single()
        persistedAppointment shouldMatch editedAppointment
    }

    @Test
    fun `On request of edit page of not existing appointment 404 error page should be returned`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointments.getEditAppointmentPage(-1, expectedStatus = HttpStatus.NOT_FOUND)

        // Then
        document shouldBePage (NotFoundErrorPage as HtmlPage)
    }

    @Test
    fun `On request to edit of not existing appointment 404 error page should be returned`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val response = therapist.appointments.editAppointment(
            -1,
            AppointmentsObjectMother.randomEditAppointmentRequest(client = ClientsObjectMother.fakeClientRef)
        )

        // Then
        response.statusCode() shouldBe HttpStatus.NOT_FOUND.value()
        response shouldBePage NotFoundErrorPage
    }

}

