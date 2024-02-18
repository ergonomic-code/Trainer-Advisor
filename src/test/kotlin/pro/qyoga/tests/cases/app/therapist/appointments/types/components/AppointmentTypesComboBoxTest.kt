package pro.qyoga.tests.cases.app.therapist.appointments.types.components

import io.kotest.inspectors.forAll
import io.kotest.matchers.should
import org.junit.jupiter.api.Test
import pro.qyoga.app.platform.components.combobox.ComboBoxItem
import pro.qyoga.l10n.systemCollator
import pro.qyoga.tests.assertions.haveElements
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentTypesObjectMother
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.platform.html.ComboBox
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
        document should haveElements("ul li", 1)
        document shouldHaveComponent ComboBox.itemFor(ComboBoxItem(appointmentType.id, appointmentType.name))
    }

    @Test
    fun `AppointmentTypesComboBox should return 5 items when no search key provided`() {
        // Given
        val pageSize = 5
        val appointments = backgrounds.appointmentTypes.createAppointmentTypes(pageSize + 1)
            .sortedWith { o1, o2 -> systemCollator.compare(o1.name, o2.name) }
            .take(pageSize)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.appointmentTypes.autocompleteSearch(null)

        // Then
        document should haveElements("ul li", pageSize)
        appointments.forAll {
            document shouldHaveComponent ComboBox.itemFor(ComboBoxItem(it.id, it.name))
        }
    }

}
