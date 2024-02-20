package pro.qyoga.tests.cases.app.therapist.appointments.core

import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.data.asiaNovosibirskTimeZone
import pro.qyoga.tests.fixture.data.randomWorkingTime
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.therapist.appointments.CalendarPage
import pro.qyoga.tests.pages.therapist.appointments.appointmentCards
import pro.qyoga.tests.pages.therapist.appointments.shouldMatch
import java.time.LocalDate


class SchedulePageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Schedule calendar page should be rendered correctly when no appointments exist`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointments.getScheduleForDay(LocalDate.now())

        // Then
        document shouldBePage CalendarPage
    }

    @Test
    fun `Schedule calendar page should be rendered correctly when appointments for all dates exists`() {
        // Given
        val today = LocalDate.now()
        val timeZone = asiaNovosibirskTimeZone
        val appointments = listOf(
            backgrounds.appointments.create(
                dateTime = today.minusDays(1).atTime(randomWorkingTime()),
                timeZone = timeZone
            ),
            backgrounds.appointments.create(
                dateTime = today.atTime(randomWorkingTime()),
                timeZone = timeZone
            ),
            backgrounds.appointments.create(
                dateTime = today.plusDays(1).atTime(randomWorkingTime()),
                timeZone = timeZone
            ),
        )

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointments.getScheduleForDay(LocalDate.now())

        // Then
        document shouldBePage CalendarPage
        document.appointmentCards() shouldMatch appointments
    }

}
