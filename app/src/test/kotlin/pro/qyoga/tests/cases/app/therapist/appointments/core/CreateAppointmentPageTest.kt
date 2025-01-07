package pro.qyoga.tests.cases.app.therapist.appointments.core

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveElement
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.data.randomWorkingTime
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.therapist.appointments.CreateAppointmentForm
import pro.qyoga.tests.pages.therapist.appointments.CreateAppointmentPage
import pro.qyoga.tests.pages.therapist.appointments.EditAppointmentForm
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

private val aDate = LocalDate.now()
private val aTime = LocalTime.now()
    .truncatedTo(ChronoUnit.HOURS)
    .plusHours(3)


class CreateAppointmentPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Create appointment page should be rendered correctly`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointments.getCreateAppointmentPage()

        // Then
        document shouldBePage CreateAppointmentPage
    }

    @Test
    fun `Create appointment page should prefill date and time, if provided`() {
        // Given
        val aDateTime = LocalDate.now().atTime(randomWorkingTime())
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointments.getCreateAppointmentPage(aDateTime)

        // Then
        CreateAppointmentForm.dateTime.value(document) shouldBe aDateTime.toString()
    }

    @Test
    fun `Creation of appointment with required-only fields should be persistent`() {
        // Given
        val client = backgrounds.clients.createClients(1).single()
        val therapeuticTask = backgrounds.therapeuticTasks.createTherapeuticTask()
        val editAppointmentRequest = AppointmentsObjectMother.randomEditAppointmentRequest(
            client = client.ref(),
            therapeuticTask = therapeuticTask.ref()
        )

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val response = therapist.appointments.createAppointment(editAppointmentRequest)

        // Then
        response.statusCode() shouldBe HttpStatus.OK.value()

        val storedAppointment =
            backgrounds.appointments.getDaySchedule(editAppointmentRequest.dateTime.toLocalDate()).single()
        storedAppointment.shouldMatch(editAppointmentRequest)
    }

    @Test
    fun `Creation of appointment with all fields should be persistent`() {
        // Given
        val client = backgrounds.clients.createClients(1).single()
        val therapeuticTask = backgrounds.therapeuticTasks.createTherapeuticTask()
        val editAppointmentRequest = AppointmentsObjectMother.randomFullEditAppointmentRequest(
            client = client.ref(),
            therapeuticTask = therapeuticTask.ref()
        )

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val response = therapist.appointments.createAppointment(editAppointmentRequest)

        // Then
        response.statusCode() shouldBe HttpStatus.OK.value()

        val storedAppointment =
            backgrounds.appointments.getDaySchedule(editAppointmentRequest.dateTime.toLocalDate()).single()
        storedAppointment.shouldMatch(editAppointmentRequest)
    }

    @Test
    fun `Creation of appointment that intersects with existing should fail with validation error`() {
        // Given
        val appointmentsDate = aDate
        val appointmentsBaseTime = aTime
        val zoneId = ZoneId.of("Asia/Novosibirsk")
        val existingAppointmentDuration = Duration.ofHours(1)

        backgrounds.appointments.create(
            dateTime = appointmentsDate.atTime(appointmentsBaseTime),
            timeZone = zoneId,
            duration = existingAppointmentDuration
        )
        val newAppointmentRequest = AppointmentsObjectMother.randomEditAppointmentRequest(
            client = backgrounds.clients.createClients(1).single().ref(),
            dateTime = appointmentsDate.atTime(appointmentsBaseTime.plus(existingAppointmentDuration.dividedBy(2))),
            timeZone = zoneId,
        )

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointments.createAppointmentForError(newAppointmentRequest)

        // Then
        document shouldBePage CreateAppointmentPage
        document shouldHave EditAppointmentForm.formPrefilledWith(newAppointmentRequest)
        document shouldHaveElement CreateAppointmentForm.appointmentsIntersectionErrorMessage
    }

}