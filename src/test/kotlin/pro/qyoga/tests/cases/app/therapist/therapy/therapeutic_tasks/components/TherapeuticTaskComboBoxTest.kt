package pro.qyoga.tests.cases.app.therapist.therapy.therapeutic_tasks.components

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import io.kotest.matchers.should
import org.junit.jupiter.api.Test
import pro.qyoga.app.platform.components.combobox.ComboBoxItem
import pro.qyoga.l10n.systemCollator
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.assertions.shouldHaveElements
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.platform.html.ComboBox
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class TherapeuticTaskComboBoxTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Response for therapeutic tasks search should contains only tasks, that contains search key in name`() {
        // Given
        val searchKey = "ГОП"
        val matchingTask =
            backgrounds.therapeuticTasks.createTherapeuticTask(taskName = randomCyrillicWord() + searchKey)
        backgrounds.therapeuticTasks.createTherapeuticTasks(taskNames = (1..2).map { randomCyrillicWord() })

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.therapeuticTasks.autocompleteSearch2(searchKey)

        // Then
        document shouldHaveComponent ComboBox.itemFor(ComboBoxItem(matchingTask.id, matchingTask.name))
        document.shouldHaveElements(ComboBox.itemsSelector, 1)
    }

    @Test
    fun `Response for therapeutic tasks search without any params should contains first 5 tasks`() {
        // Given
        val pageSize = 5
        val tasksPage = backgrounds.therapeuticTasks.createTherapeuticTasks(taskNames = (1..(pageSize + 1)).map {
            randomCyrillicWord(minLength = 3)
        })
            .sortedWith { o1, o2 -> systemCollator.compare(o1.name, o2.name) }
            .take(pageSize)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.therapeuticTasks.autocompleteSearch2(null)

        // Then
        document should Matcher.all(
            *tasksPage.map { ComboBox.itemFor(ComboBoxItem(it.id, it.name)).matcher() }
                .toTypedArray()
        )
    }

}