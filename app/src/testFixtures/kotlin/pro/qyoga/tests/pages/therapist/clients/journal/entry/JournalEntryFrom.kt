package pro.qyoga.tests.pages.therapist.clients.journal.entry

import pro.qyoga.app.therapist.clients.journal.edit_entry.create.CreateJournalEntryPageController
import pro.qyoga.tests.platform.html.*

abstract class JournalEntryForm(action: FormAction) : QYogaForm("journalEntryForm", action) {

    val version = Input.hidden("version", true)
    val dateInput = Input.text("date", true)
    val therapeuticTaskNameInput = Input.text("therapeuticTaskName", true)
    val entryTextInput = TextArea("journalEntryText", true)

    private val addButton = Button("confirmButton", "Сохранить")

    object FormDraftScript : Script("formDraft") {
        val clientId = Variable("clientId")
        val entryId = Variable("entryId")
        val entryDate = Variable("entryDate")
        val serverState = Variable("serverState")
        override val vars = listOf(clientId, serverState, entryId, entryDate)
    }

    companion object {
        const val DUPLICATED_DATE_MESSAGE = "div.invalid-feedback:contains(Запись за эту дату уже существует)"
    }

    override val components = listOf(
        dateInput,
        therapeuticTaskNameInput,
        entryTextInput,
        addButton,
        FormDraftScript
    )

}

object CreateJournalEntryForm :
    JournalEntryForm(FormAction.hxPost(CreateJournalEntryPageController.CREATE_JOURNAL_PAGE_URL))

object EditJournalEntryForm : JournalEntryForm(FormAction.hxPost(EditJournalEntryPage.PATH))
