package pro.qyoga.tests.cases.app.therapist.appointments.types.components

import org.junit.jupiter.api.Test
import pro.qyoga.app.components.combobox.ComboBoxItem
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.appointments.AppointmentTypesObjectMother
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.infra.html.ComboBox
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class AppointmentTypesComboBoxTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `AppointmentTypesComboBox should return items that contain keyword in title`() {
        // Given
        val searchKey = "Трен"
        val appointmentType =
            backgrounds.appointmentTypes.createAppointmentType(AppointmentTypesObjectMother.randomAppointmentType(name = searchKey + randomCyrillicWord()))
        backgrounds.appointmentTypes.createAppointmentTypes(3)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointmentTypes.autocompleteSearch(searchKey)

        // Then
        document shouldHaveComponent ComboBox.itemFor(ComboBoxItem(appointmentType.id, appointmentType.name))
    }

}
