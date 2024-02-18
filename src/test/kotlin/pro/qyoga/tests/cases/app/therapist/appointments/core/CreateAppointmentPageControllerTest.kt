package pro.qyoga.tests.cases.app.therapist.appointments.core

import io.kotest.inspectors.forAny
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.qyoga.app.therapist.appointments.core.edit.CreateAppointmentPageController
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageController
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother.randomEditAppointmentRequest
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.infra.web.QYogaAppBaseTest
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

private val asiaNovosibirsk = ZoneId.of("Asia/Novosibirsk")
private val europeMoscow = ZoneId.of("Europe/Moscow")
private val timeZonesDiff = 4

private val aDate = LocalDate.now()
private val aDateTime = aDate.atTime(
    LocalTime.now()
        .truncatedTo(ChronoUnit.HOURS)
        .plusHours(3)
)

class CreateAppointmentPageControllerTest : QYogaAppBaseTest() {

    private val controller = getBean<CreateAppointmentPageController>()

    @Test
    fun `Creation of appointment, that intersects with an appointment in another time zone should fail`() {
        // Given
        val existingAppointmentTimeZone = asiaNovosibirsk
        val newAppointmentTimeZone = europeMoscow
        val existingAppointmentLocalDateTime = aDateTime

        backgrounds.appointments.create(
            dateTime = existingAppointmentLocalDateTime,
            timeZone = existingAppointmentTimeZone
        )

        val createNewAppointmentRequest = randomEditAppointmentRequest(
            client = ClientsObjectMother.fakeClientRef,
            dateTime = aDate.atTime(existingAppointmentLocalDateTime.get(ChronoField.HOUR_OF_DAY) - timeZonesDiff, 0),
            timeZone = newAppointmentTimeZone
        )

        // When
        val result = controller.createAppointment(
            createNewAppointmentRequest,
            theTherapistUserDetails
        )

        // Then
        result.shouldBeIntersectionError()
        backgrounds.appointments.getDaySchedule(aDate) shouldHaveSize 1
    }

    @Test
    fun `Creation of appointment, that starts just after existing appointment in the same time zone should succeed`() {
        // Given
        val appointmentsTimeZone = asiaNovosibirsk
        val existingAppointmentDateTime = aDateTime
        val existingAppointmentDuration = Duration.ofHours(1)
        val newAppointmentDateTime = existingAppointmentDateTime.plus(existingAppointmentDuration)

        backgrounds.appointments.create(
            dateTime = existingAppointmentDateTime,
            duration = existingAppointmentDuration,
            timeZone = appointmentsTimeZone
        )

        val createNewAppointmentRequest = randomEditAppointmentRequest(
            client = backgrounds.clients.aClient().ref(),
            dateTime = newAppointmentDateTime,
            timeZone = appointmentsTimeZone
        )

        // When
        val result = controller.createAppointment(createNewAppointmentRequest, theTherapistUserDetails)

        // Then
        result.shouldBeSuccess()
        backgrounds.appointments.getDaySchedule(aDate).toList().forAny { it shouldMatch createNewAppointmentRequest }
    }

    @Test
    fun `Creation of appointment, that starts just after existing appointment in another time zone should succeed`() {
        // Given
        val existingAppointmentTimeZone = asiaNovosibirsk
        val anotherAppointmentTimeZone = europeMoscow
        val existingAppointmentDateTime = aDateTime
        val existingAppointmentDuration = Duration.ofHours(1)
        val newAppointmentDateTime = aDate.atTime(existingAppointmentDateTime.hour - timeZonesDiff + 1, 0)

        backgrounds.appointments.create(
            dateTime = existingAppointmentDateTime,
            duration = existingAppointmentDuration,
            timeZone = existingAppointmentTimeZone
        )

        val createNewAppointmentRequest = randomEditAppointmentRequest(
            client = backgrounds.clients.aClient().ref(),
            dateTime = newAppointmentDateTime,
            timeZone = anotherAppointmentTimeZone
        )

        // When
        val result = controller.createAppointment(createNewAppointmentRequest, theTherapistUserDetails)

        // Then
        result.shouldBeSuccess()
        backgrounds.appointments.getDaySchedule(aDate).toList().forAny { it shouldMatch createNewAppointmentRequest }
    }

    @Test
    fun `Creation of appointment, that ends just before existing appointment in the same time zone should succeed`() {
        // Given
        val appointmentsTimeZone = asiaNovosibirsk
        val existingAppointmentDateTime = aDateTime
        val newAppointmentDuration = Duration.ofHours(1)
        val newAppointmentDateTime = existingAppointmentDateTime.minus(newAppointmentDuration)

        backgrounds.appointments.create(
            dateTime = existingAppointmentDateTime,
            timeZone = appointmentsTimeZone
        )

        val createNewAppointmentRequest = randomEditAppointmentRequest(
            client = backgrounds.clients.aClient().ref(),
            dateTime = newAppointmentDateTime,
            timeZone = appointmentsTimeZone,
            duration = newAppointmentDuration
        )

        // When
        val result = controller.createAppointment(createNewAppointmentRequest, theTherapistUserDetails)

        // Then
        result.shouldBeSuccess()
        backgrounds.appointments.getDaySchedule(aDate).toList().forAny { it shouldMatch createNewAppointmentRequest }
    }

    @Test
    fun `Creation of appointment, that ends just before existing appointment in another time zone should succeed`() {
        // Given
        val existingAppointmentTimeZone = asiaNovosibirsk
        val anotherAppointmentTimeZone = europeMoscow
        val existingAppointmentDateTime = aDateTime
        val newAppointmentDuration = Duration.ofHours(1)
        val newAppointmentDateTime = aDate.atTime(existingAppointmentDateTime.hour - timeZonesDiff - 2, 0)

        backgrounds.appointments.create(
            dateTime = existingAppointmentDateTime,
            timeZone = existingAppointmentTimeZone
        )

        val createNewAppointmentRequest = randomEditAppointmentRequest(
            client = backgrounds.clients.aClient().ref(),
            dateTime = newAppointmentDateTime,
            timeZone = anotherAppointmentTimeZone,
            duration = newAppointmentDuration
        )

        // When
        val result = controller.createAppointment(createNewAppointmentRequest, theTherapistUserDetails)

        // Then
        result.shouldBeSuccess()
        backgrounds.appointments.getDaySchedule(aDate).toList().forAny { it shouldMatch createNewAppointmentRequest }
    }

    private fun Any.shouldBeIntersectionError() {
        shouldBeInstanceOf<ModelAndView>()
        model["appointmentsIntersectionError"] shouldBe true
    }

    private fun Any.shouldBeSuccess() {
        shouldBeInstanceOf<ResponseEntity<Unit>>()
        headers["Hx-Redirect"]?.single() shouldBe SchedulePageController.PATH
    }

}