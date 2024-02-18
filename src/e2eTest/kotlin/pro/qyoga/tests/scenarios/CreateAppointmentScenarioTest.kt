package pro.qyoga.tests.scenarios

import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.Selenide.open
import com.codeborne.selenide.SetValueOptions
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeEmpty
import org.junit.jupiter.api.Test
import pro.qyoga.tests.pages.publc.LoginPage
import pro.qyoga.tests.pages.therapist.appointments.CreateAppointmentForm
import pro.qyoga.tests.pages.therapist.appointments.EmptyFutureSchedulePage
import pro.qyoga.tests.pages.therapist.appointments.FutureSchedulePageTab
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_LOGIN
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_PASSWORD
import pro.qyoga.tests.infra.QYogaE2EBaseTest
import pro.qyoga.tests.platform.html.ComboBox
import pro.qyoga.tests.platform.selenide.`$`
import pro.qyoga.tests.platform.selenide.fastType
import java.time.LocalDate


class CreateAppointmentScenarioTest : QYogaE2EBaseTest() {

    @Test
    fun `Create appointment scenario`() {
        // Given
        val aClient = backgrounds.clients.aClient()
        theTherapistIsLoggedIn()

        // When
        userOnCreateAppointmentPage()

        // Then
        `$`(CreateAppointmentForm.timeZone.titleInput).`val`().shouldNotBeEmpty()

        // And When
        selectComboBoxItem(CreateAppointmentForm.clientInput, aClient.firstName)
        `$`(CreateAppointmentForm.typeInput.titleInput).`val`("Тренировка")
        `$`(CreateAppointmentForm.dateTime).setValue(SetValueOptions.withDateTime(LocalDate.now().atTime(9, 0)))
        `$`(CreateAppointmentForm.submit).click()

        // Then
        `$`(FutureSchedulePageTab.TODAY_APPOINTMENT_ROWS).text() shouldContain aClient.fullName()
    }

    private fun theTherapistIsLoggedIn() {
        open("$baseUri${LoginPage.path}")

        `$`(LoginPage.LoginForm.username).fastType(THE_THERAPIST_LOGIN)
        `$`(LoginPage.LoginForm.password).fastType(THE_THERAPIST_PASSWORD)
        `$`(LoginPage.LoginForm.submit).click()
    }

    private fun userOnCreateAppointmentPage() {
        `$`(EmptyFutureSchedulePage.addAppointmentLink).click()
    }

    private fun selectComboBoxItem(comboBox: ComboBox, value: String) {
        `$`(comboBox.titleInput).fastType(value)
        `$`(comboBox.selector()).find(ComboBox.itemsSelector).click()
    }

}
