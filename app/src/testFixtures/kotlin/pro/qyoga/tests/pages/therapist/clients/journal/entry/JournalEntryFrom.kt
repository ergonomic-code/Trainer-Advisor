package pro.qyoga.tests.pages.therapist.clients.journal.entry

import pro.qyoga.tests.platform.html.*

abstract class JournalEntryFrom(action: FormAction) : QYogaForm("journalEntryFrom", action) {

    val dateInput = Input.text("date", true)
    val therapeuticTaskNameInput = Input.text("therapeuticTaskName", true)
    val entryTextInput = TextArea("journalEntryText", true)

    private val addButton = Button("confirmButton", "Сохранить")

    companion object {
        const val DUPLICATED_DATE_MESSAGE = "div.invalid-feedback:contains(Запись за эту дату уже существует)"
    }

    override val components = listOf(
        dateInput,
        therapeuticTaskNameInput,
        entryTextInput,
        addButton
    )

}

object CreateJournalEntryForm : JournalEntryFrom(FormAction.hxPost(CreateJournalEntryPage.PATH))

object EditJournalEntryForm : JournalEntryFrom(FormAction.hxPost(EditJournalEntryPage.PATH))
