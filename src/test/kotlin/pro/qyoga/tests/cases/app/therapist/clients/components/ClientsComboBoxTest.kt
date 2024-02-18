package pro.qyoga.tests.cases.app.therapist.clients.components

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import io.kotest.matchers.should
import org.junit.jupiter.api.Test
import pro.qyoga.app.platform.components.combobox.ComboBoxItem
import pro.qyoga.l10n.systemCollator
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.assertions.shouldHaveElements
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother.createClientCardDto
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.platform.html.ComboBox
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class ClientsComboBoxTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Response for clients search should contains only clients, that contains search key in name`() {
        // Given
        val searchKey = "вна"

        val matchingClient =
            backgrounds.clients.createClients(listOf(createClientCardDto(lastName = randomCyrillicWord() + searchKey)))
                .single()

        backgrounds.clients.createClients(3)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clients.autocompleteClients(searchKey)

        // Then
        document shouldHaveComponent ComboBox.itemFor(
            ComboBoxItem(
                matchingClient.id,
                matchingClient.fullName(),
                matchingClient.phoneNumber
            )
        )
        document.shouldHaveElements(ComboBox.itemsSelector, 1)
    }

    @Test
    fun `Response for clients search without any params should contains first 5 clients`() {
        // Given
        val pageSize = 5
        val clientsPage = backgrounds.clients.createClients(pageSize + 1)
            .sortedWith { o1, o2 -> systemCollator.compare(o1.lastName, o2.lastName) }
            .take(pageSize)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clients.autocompleteClients(null)

        // Then
        document should Matcher.all(
            *clientsPage.map { ComboBox.itemFor(ComboBoxItem(it.id, it.fullName(), it.phoneNumber)).matcher() }
                .toTypedArray()
        )
    }

}