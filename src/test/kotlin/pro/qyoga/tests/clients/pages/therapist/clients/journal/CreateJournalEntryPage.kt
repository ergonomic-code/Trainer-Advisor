package pro.qyoga.tests.clients.pages.therapist.clients.journal

import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.infra.html.*


object CreateJournalEntryPage : Component {

    const val PATH = "/therapist/clients/{id}/journal/create"

    object JournalEntryFrom : QYogaForm("journalEntryFrom", FormAction.hxPost(PATH)) {

        val dateInput = Input.text("date", true)
        val therapeuticTaskNameInput = Input.text("therapeuticTaskName", true)
        val entryTextInput = TextArea("journalEntryText", true)

        const val DUPLICATED_DATE_MESSAGE = "div.invalid-feedback:contains(Запись за эту дату уже существует)"

        override val components = listOf(
            dateInput,
            therapeuticTaskNameInput,
            entryTextInput
        )

    }

    override fun selector() = "#createJournalEntryTabContent"

    override fun match(element: Element) {
        element shouldHaveComponent JournalEntryFrom
    }

}