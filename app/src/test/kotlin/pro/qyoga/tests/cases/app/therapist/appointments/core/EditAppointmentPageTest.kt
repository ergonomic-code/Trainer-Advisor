package pro.qyoga.tests.cases.app.therapist.appointments.core

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageController
import pro.qyoga.core.appointments.core.toEditRequest
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveElement
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother.aAppointmentId
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother.randomEditAppointmentRequest
import pro.qyoga.tests.fixture.object_mothers.appointments.randomAppointmentCost
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.pages.therapist.appointments.CalendarPage
import pro.qyoga.tests.pages.therapist.appointments.CreateAppointmentForm
import pro.qyoga.tests.pages.therapist.appointments.EditAppointmentForm
import pro.qyoga.tests.pages.therapist.appointments.EditAppointmentPage
import pro.qyoga.tests.platform.html.HtmlPage
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId


class EditAppointmentPageTest : QYogaAppIntegrationBaseTest() {

    private val timeZones = getBean<TimeZones>()

    @Test
    fun `Edit page for minimal appointment page should be correctly rendered and prefilled`() {
        // Given
        val appointment = backgrounds.appointments.create()

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointments.getEditAppointmentPage(appointment.ref())

        // Then
        document shouldBePage EditAppointmentPage
        document shouldHave EditAppointmentForm.formPrefilledWith(appointment.toEditRequest(timeZones::findById))
    }

    @Test
    fun `Edit page for full appointment page should be correctly rendered and prefilled`() {
        // Given
        val appointment = backgrounds.appointments.createFull()

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointments.getEditAppointmentPage(appointment.ref())

        // Then
        document shouldBePage EditAppointmentPage
        document shouldHave EditAppointmentForm.formPrefilledWith(appointment.toEditRequest(timeZones::findById))
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
        val response = therapist.appointments.editAppointment(appointment.ref(), editedAppointment)

        // Then
        response.statusCode() shouldBe HttpStatus.OK.value()
        response shouldBePage CalendarPage

        val persistedAppointment =
            backgrounds.appointments.getDaySchedule(editedAppointment.dateTime.toLocalDate()).single()
        persistedAppointment.shouldMatch(editedAppointment)
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
        val response = therapist.appointments.editAppointment(appointment.ref(), editedAppointment)

        // Then
        response.statusCode() shouldBe HttpStatus.OK.value()
        response shouldBePage CalendarPage

        val persistedAppointment = backgrounds.appointments.findAll().single()
        persistedAppointment shouldMatch editedAppointment
    }

    @Test
    fun `On request of edit page of not existing appointment 404 error page should be returned`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document =
            therapist.appointments.getEditAppointmentPage(aAppointmentId(), expectedStatus = HttpStatus.NOT_FOUND)

        // Then
        document shouldBePage (NotFoundErrorPage as HtmlPage)
    }

    @Test
    fun `On request to edit of not existing appointment 404 error page should be returned`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        val editAppointmentRequest = randomEditAppointmentRequest()

        // When
        val response = therapist.appointments.editAppointment(
            aAppointmentId(), editAppointmentRequest
        )

        // Then
        response.statusCode() shouldBe HttpStatus.NOT_FOUND.value()
        response shouldBePage NotFoundErrorPage
    }

    @Test
    fun `Creation of appointment that intersects with existing should fail with validation error`() {
        // Given
        val appointmentsDate = LocalDate.of(2024, 2, 11)
        val appointmentsBaseTime = LocalTime.of(9, 0)
        val zoneId = ZoneId.of("Asia/Novosibirsk")
        val existingAppointmentDuration = Duration.ofHours(1)

        val existingAppointment = backgrounds.appointments.create(
            dateTime = appointmentsDate.atTime(appointmentsBaseTime),
            timeZone = zoneId,
            duration = existingAppointmentDuration
        )
        val nextNotRescheduledAppointment = backgrounds.appointments.create(
            dateTime = appointmentsDate.atTime(appointmentsBaseTime).plus(existingAppointmentDuration),
            timeZone = zoneId,
            duration = existingAppointmentDuration
        )
        val rescheduleAppointmentRequest = nextNotRescheduledAppointment.copy(dateTime = existingAppointment.dateTime)
            .toEditRequest(timeZones::findById)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointments.editAppointmentForError(
            nextNotRescheduledAppointment.ref(),
            rescheduleAppointmentRequest
        )

        // Then
        document shouldBePage EditAppointmentPage
        document shouldHave EditAppointmentForm.formPrefilledWith(rescheduleAppointmentRequest)
        document shouldHaveElement CreateAppointmentForm.appointmentsIntersectionErrorMessage

        val storedNextAppointment = backgrounds.appointments.findById(nextNotRescheduledAppointment.id)!!
        storedNextAppointment shouldMatch nextNotRescheduledAppointment
    }

    @Test
    fun `Deletion of appointment should be persistent and return redirect to specified calendar day`() {
        // Given
        val appointment = backgrounds.appointments.create()
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val response = therapist.appointments.delete(appointment.ref(), appointment.wallClockDateTime.toLocalDate())

        // Then
        response.statusCode shouldBe HttpStatus.OK.value()
        response.header("HX-Location") shouldBe SchedulePageController.DATE_PATH.replace(
            "{date}",
            appointment.wallClockDateTime.toLocalDate().toString()
        )

        backgrounds.appointments.getDaySchedule(appointment.wallClockDateTime.toLocalDate()).shouldBeEmpty()
    }

}

