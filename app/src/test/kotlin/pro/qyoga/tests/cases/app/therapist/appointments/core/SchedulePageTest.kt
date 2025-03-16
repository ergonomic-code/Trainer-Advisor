package pro.qyoga.tests.cases.app.therapist.appointments.core

import com.fasterxml.jackson.core.type.TypeReference
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.data.asiaNovosibirskTimeZone
import pro.qyoga.tests.fixture.data.randomWorkingTime
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.therapist.appointments.CalendarPage
import pro.qyoga.tests.pages.therapist.appointments.appointmentCards
import pro.qyoga.tests.pages.therapist.appointments.shouldMatch
import java.time.LocalDate


@DisplayName("Страница календаря")
class SchedulePageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `должна корректно рендерить пустой календарь`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()

        // Действие
        val document = therapist.appointments.getScheduleForDay(LocalDate.now())

        // Проверка
        document shouldBePage CalendarPage
    }

    @Test
    fun `должна корректно рендерить карточки приёмов за каждый день календаря`() {
        // Сетап
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

        // Действие
        val document = therapist.appointments.getScheduleForDay(today)

        // Проверка
        document shouldBePage CalendarPage
        document.appointmentCards() shouldMatch appointments
    }

    @Test
    fun `должна содержать корректное значение переменной идентификатора приёма для фокуса`() {
        // Сетап
        val today = LocalDate.now()
        val app = backgrounds.appointments.create(
            dateTime = today.minusDays(1).atTime(randomWorkingTime())
        )

        val therapist = TherapistClient.loginAsTheTherapist()

        // Действие
        val document = therapist.appointments.getScheduleForDay(today, app.ref())

        // Проверка
        document shouldBePage CalendarPage
        val appToFocus = CalendarPage.RevealAppointmentScript.appToFocus.value(
            document.getElementById(CalendarPage.RevealAppointmentScript.id)!!,
            object : TypeReference<String?>() {})

        appToFocus shouldBe app.id.toString()
    }

}
