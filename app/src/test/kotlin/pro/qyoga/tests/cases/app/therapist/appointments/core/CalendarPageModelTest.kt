package pro.qyoga.tests.cases.app.therapist.appointments.core

import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import pro.qyoga.app.therapist.appointments.core.schedule.CalendarPageModel
import pro.qyoga.app.therapist.appointments.core.schedule.TimeMark
import pro.qyoga.tests.fixture.data.asiaNovosibirskTimeZone
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother.randomAppointment
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

class CalendarPageModelTest {

    @Test
    fun `Calendar should start at first appointment time`() {
        // Given
        val firstAppointmentStartTime = LocalTime.of(2, 0)
        val today = LocalDate.now()
        val appointments = listOf(
            randomAppointment(
                dateTime = today.atTime(firstAppointmentStartTime), timeZone = asiaNovosibirskTimeZone
            ), randomAppointment(
                dateTime = today.atTime(8, 0), timeZone = asiaNovosibirskTimeZone
            )
        )

        // When
        val calendarPageModel = CalendarPageModel.of(today, appointments)

        // Then
        calendarPageModel.timeMarks.first().time shouldBe firstAppointmentStartTime
    }


    @Test
    fun `Calendar should end after last appointment end time`() {
        // Given
        val lastAppointmentStartTime = LocalTime.of(22, 15)
        val today = LocalDate.now()
        val lastAppointmentDuration = Duration.ofMinutes(15)
        val appointments = listOf(
            randomAppointment(
                dateTime = today.atTime(8, 0), timeZone = asiaNovosibirskTimeZone
            ), randomAppointment(
                dateTime = today.atTime(lastAppointmentStartTime),
                timeZone = asiaNovosibirskTimeZone,
                duration = lastAppointmentDuration
            )
        )

        // When
        val calendarPageModel = CalendarPageModel.of(today, appointments)

        // Then
        val lastTimeMark = calendarPageModel.timeMarks.last()
        (lastTimeMark.time + TimeMark.length) shouldBeGreaterThanOrEqualTo (lastAppointmentStartTime + lastAppointmentDuration)
    }

}