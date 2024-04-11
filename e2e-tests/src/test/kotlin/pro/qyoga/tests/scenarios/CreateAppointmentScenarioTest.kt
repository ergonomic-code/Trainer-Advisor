package pro.qyoga.tests.scenarios

import com.codeborne.selenide.Selenide.`$`
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldNotBeEmptyInput
import pro.qyoga.tests.infra.QYogaE2EBaseTest
import pro.qyoga.tests.pages.therapist.appointments.CalendarPage
import pro.qyoga.tests.pages.therapist.appointments.CreateAppointmentForm
import pro.qyoga.tests.platform.selenide.click
import pro.qyoga.tests.platform.selenide.find
import pro.qyoga.tests.platform.selenide.selectIn
import pro.qyoga.tests.platform.selenide.typeInto
import pro.qyoga.tests.scripts.loginAsTheTherapist


@DisplayName("Создание приёма")
class CreateAppointmentScenarioTest : QYogaE2EBaseTest() {

    @DisplayName("Успешное создание")
    @Test
    fun successfulAppointmentCreation() {
        // Фикстура
        val aClient = backgrounds.clients.aClient()
        loginAsTheTherapist()

        // Терапевт кликает по ячейке календаря
        click(CalendarPage.addAppointmentLink)

        // И видит предзаполненное значение поля ввода таймзоны
        find(CreateAppointmentForm.timeZone.titleInput).shouldNotBeEmptyInput()

        // Затем заполняет обязательные поля и отправляет форму
        selectIn(CreateAppointmentForm.clientInput, aClient.firstName)
        typeInto(CreateAppointmentForm.typeInput.titleInput, "Тренировка")
        click(CreateAppointmentForm.submit)

        // После чего видит календарь с карточкой приёма выбранного клиента
        `$`(CalendarPage.APPOINTMENT_CARD_SELECTOR).text() shouldContain aClient.fullName()
    }

}
