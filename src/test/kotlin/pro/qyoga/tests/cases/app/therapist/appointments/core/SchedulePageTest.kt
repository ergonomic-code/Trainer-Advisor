package pro.qyoga.tests.cases.app.therapist.appointments.core

import io.kotest.matchers.shouldHave
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.therapist.appointments.EmptyFutureSchedulePage
import pro.qyoga.tests.clients.pages.therapist.appointments.FutureSchedulePage
import pro.qyoga.tests.clients.pages.therapist.appointments.PastSchedulePage
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime


class SchedulePageTest : QYogaAppIntegrationBaseTest() {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun `Future schedule page should be rendered correctly when no appointments exist`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointments.getFutureAppointmentsSchedule()

        // Then
        document shouldBePage EmptyFutureSchedulePage
    }

    @Test
    fun `Future schedule page should be rendered correctly when appointments are exist`() {
        // Given
        val today = ZonedDateTime.of(LocalDate.now(), LocalTime.of(9, 30), ZoneId.systemDefault())
        val dates = listOf(
            // Today
            today,
            today.plusMinutes(90),

            // Next week
            today.plusDays(1).plusMinutes(105),
            today.plusDays(6).plusMinutes(120),

            // Later
            today.plusDays(7),
            today.plusDays(14)
        )
        val appointments = dates.map {
            backgrounds.appointments.create(
                dateTime = it.toLocalDateTime(),
                timeZone = ZoneId.systemDefault()
            )
        }
        val todayAppointments = appointments.slice((0..1))
        val nextWeekAppointments = appointments.slice((2..3))
        val laterAppointments = appointments.slice((4..5))
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointments.getFutureAppointmentsSchedule()

        // Then
        document shouldBePage FutureSchedulePage
        FutureSchedulePage.today(document) shouldHave FutureSchedulePage.rowsFor(todayAppointments)
        FutureSchedulePage.nextWeek(document) shouldHave FutureSchedulePage.rowsFor(nextWeekAppointments)
        FutureSchedulePage.later(document) shouldHave FutureSchedulePage.rowsFor(laterAppointments)
    }

    @Test
    fun `Past schedule page should be rendered correctly when no appointments exist`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointments.getPastAppointmentsSchedule()

        // Then
        document shouldBePage PastSchedulePage
    }

    @Test
    fun `Past schedule page should contain 10 latest appointments`() {
        // Given
        val page = 10
        val today = LocalDate.now()
        val appointmentDates = generateSequence(today) { it.minusDays(1) }
            .drop(1)
            .take(page + 1)

        val appointments = appointmentDates
            .map { backgrounds.appointments.create(dateTime = it.atTime(9, 30), timeZone = ZoneId.systemDefault()) }
            .toList()
        log.info("Appointments:\n{}", appointments.joinToString("\n"))
        val appointmentsPage = appointments.take(page)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointments.getPastAppointmentsSchedule()

        // Then
        document shouldBePage PastSchedulePage
        document shouldHave PastSchedulePage.rowsFor(appointmentsPage)
    }


}
