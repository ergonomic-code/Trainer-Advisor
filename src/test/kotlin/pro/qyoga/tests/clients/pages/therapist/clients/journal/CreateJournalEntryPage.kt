package pro.qyoga.tests.clients.pages.therapist.clients.journal

import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.infra.html.*


object CreateJournalEntryPage : Component {

    val path = "/therapist/clients/{id}/journal/create"

    object JournalEntryFrom : QYogaForm("journalEntryFrom", FormAction.classicPost(path)) {

        val dateInput = Input.text("date", true)
        val therapeuticTaskNameInput = Input.text("therapeuticTaskName", true)
        val entryTextInput = TextArea("journalEntryText", true)

        override val components = listOf(
            dateInput,
            therapeuticTaskNameInput,
            entryTextInput
        )

    }

    override fun selector() = "#createJournalEntryTabContent"

    override fun match(element: Element) {
        element shouldHave JournalEntryFrom
    }

}