package pro.qyoga.tests.cases.app.therapist.appointments.core

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.qyoga.app.therapist.appointments.core.schedule.AppointmentCard
import pro.qyoga.app.therapist.appointments.core.schedule.CalendarPageModel
import pro.qyoga.app.therapist.appointments.core.schedule.GetCalendarAppointmentsRs
import pro.qyoga.app.therapist.appointments.core.schedule.TimeMark
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother.randomLocalizedAppointmentSummary
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

@DisplayName("Модель страницы календаря")
class CalendarPageModelTest {

    @Test
    fun `должна содержать временные метки начиная со времени первого приёма`() {
        // Сетап
        val firstAppointmentStartTime = LocalTime.of(2, 0)
        val today = LocalDate.now()
        val appointments = listOf(
            randomLocalizedAppointmentSummary(
                dateTime = today.atTime(firstAppointmentStartTime),
            ),
            randomLocalizedAppointmentSummary(
                dateTime = today.atTime(8, 0),
            )
        )

        // Действие
        val calendarPageModel = CalendarPageModel.of(today, GetCalendarAppointmentsRs(appointments, false))

        // Проверка
        calendarPageModel.timeMarks.first().time shouldBe firstAppointmentStartTime
    }


    @Test
    fun `должна содержать временные метки до времени окончания последнего приёма включительно`() {
        // Сетап
        val lastAppointmentStartTime = LocalTime.of(22, 15)
        val today = LocalDate.now()
        val lastAppointmentDuration = Duration.ofMinutes(15)
        val appointments = listOf(
            randomLocalizedAppointmentSummary(
                dateTime = today.atTime(8, 0),
            ),
            randomLocalizedAppointmentSummary(
                dateTime = today.atTime(lastAppointmentStartTime),

                duration = lastAppointmentDuration
            )
        )

        // Действие
        val calendarPageModel = CalendarPageModel.of(today, GetCalendarAppointmentsRs(appointments, false))

        // Проверка
        val lastTimeMark = calendarPageModel.timeMarks.last()
        (lastTimeMark.time + TimeMark.length) shouldBeGreaterThanOrEqualTo (lastAppointmentStartTime + lastAppointmentDuration)
    }

    @Test
    fun `должна содержать временные метки начиная с полуночи, в случае если содержит приём переходящий на следующие сутки в рамках календаря`() {
        // Сетап
        val appointmentDuration = Duration.ofHours(2)
        val appointmentStartTime = LocalTime.MIDNIGHT
            .minusHours(appointmentDuration.dividedBy(2).toHours())
        val today = LocalDate.now()
        val appointments = listOf(
            randomLocalizedAppointmentSummary(
                dateTime = today.atTime(appointmentStartTime),
                duration = appointmentDuration
            )
        )

        // Действие
        val calendarPageModel = CalendarPageModel.of(today, GetCalendarAppointmentsRs(appointments, false))

        // Проверка
        calendarPageModel.timeMarks.first().time shouldBe LocalTime.MIDNIGHT
    }

    @Test
    fun `должна содержать временные метки начиная с полуночи, в случае если содержит приём начинающийся в предыдущие сутки в рамках календаря`() {
        // Сетап
        val appointmentDuration = Duration.ofHours(2)
        val appointmentStartTime = LocalTime.MIDNIGHT
            .minusHours(appointmentDuration.dividedBy(2).toHours())
        val yesterday = LocalDate.now().minusDays(1)
        val appointments = listOf(
            randomLocalizedAppointmentSummary(
                dateTime = yesterday.atTime(appointmentStartTime),
                duration = appointmentDuration
            )
        )

        // Действие
        val calendarPageModel = CalendarPageModel.of(yesterday, GetCalendarAppointmentsRs(appointments, false))

        // Проверка
        calendarPageModel.timeMarks.first().time shouldBe LocalTime.MIDNIGHT
    }

    @Test
    fun `должна содержать временные метки начиная со стандартного времени начала календаря, в случае если содержит приём, заканчивающийся за пределами календаря`() {
        // Сетап
        val appointmentDuration = Duration.ofHours(2)
        val appointmentStartTime = LocalTime.MIDNIGHT
            .minusHours(appointmentDuration.dividedBy(2).toHours())
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val appointments = listOf(
            randomLocalizedAppointmentSummary(
                dateTime = tomorrow.atTime(appointmentStartTime),
                duration = appointmentDuration
            )
        )

        // Действие
        val calendarPageModel = CalendarPageModel.of(today, GetCalendarAppointmentsRs(appointments, false))

        // Проверка
        calendarPageModel.timeMarks.first().time.hour shouldBe CalendarPageModel.DEFAULT_START_HOUR
        calendarPageModel.timeMarks.last().time.hour shouldBe 23
    }

    @Test
    fun `должна содержать временные метки до стандартного времени окончания календаря, в случае если содержит приём, начинающийся за пределами календаря`() {
        // Сетап
        val appointmentDuration = Duration.ofHours(2)
        val appointmentStartTime = LocalTime.MIDNIGHT
            .minusHours(appointmentDuration.dividedBy(2).toHours())
        val today = LocalDate.now()
        val dayBefore = today.minusDays(2)
        val appointments = listOf(
            randomLocalizedAppointmentSummary(
                dateTime = dayBefore.atTime(appointmentStartTime),
                duration = appointmentDuration
            )
        )

        // Действие
        val calendarPageModel = CalendarPageModel.of(today, GetCalendarAppointmentsRs(appointments, false))

        // Проверка
        calendarPageModel.timeMarks.first().time shouldBe LocalTime.MIDNIGHT
        calendarPageModel.timeMarks.last().time.hour shouldBe CalendarPageModel.DEFAULT_END_HOUR
    }

    @Test
    fun `должна иметь карточку приёма заканчивающуюся в конце дня, в случае перехода приёма на следующий день`() {
        // Сетап
        val appointmentDuration = Duration.ofHours(2)
        val appointmentStartTime = LocalTime.MIDNIGHT
            .minusHours(appointmentDuration.dividedBy(2).toHours())
        val today = LocalDate.now()
        val app = randomLocalizedAppointmentSummary(
            dateTime = today.atTime(appointmentStartTime),
            duration = appointmentDuration
        )

        // Действие
        val card = AppointmentCard(app, today)

        // Проверка
        (card.timeMarkOffsetPercent + card.timeMarkLengthPercent).shouldBe(4.0)
    }

    @Test
    fun `должна иметь две карточки приёма, в случае если он накладывается на два дня календаря`() {
        // Сетап
        val appointmentDuration = Duration.ofHours(2)
        val appointmentStartTime = LocalTime.MIDNIGHT
            .minusHours(appointmentDuration.dividedBy(2).toHours())
        val today = LocalDate.now()
        val appointments = listOf(
            randomLocalizedAppointmentSummary(
                dateTime = today.atTime(appointmentStartTime),
                duration = appointmentDuration
            )
        )

        // Действие
        val calendarPageModel = CalendarPageModel.of(today, GetCalendarAppointmentsRs(appointments, false))

        // Проверка
        calendarPageModel.appointmentCards() shouldHaveSize 2
    }

}

fun CalendarPageModel.appointmentCards() =
    timeMarks.flatMap { it.days }.mapNotNull { it.second }
