package pro.qyoga.app.therapist.clients.journal.edit_entry.edit

import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.viewId
import pro.qyoga.app.therapist.clients.journal.edit_entry.shared.JOURNAL_ENTRY_VIEW_NAME
import pro.qyoga.core.clients.cards.model.ClientRef
import pro.qyoga.core.clients.journals.dtos.EditJournalEntryRq
import pro.qyoga.core.clients.journals.model.JournalEntry


data class EditJournalEntryPageModel(
    val clientRef: ClientRef,
    val entry: JournalEntry,
    val formAction: String,
    val fragment: String? = null,
    val duplicatedDate: Boolean = false,
) : ModelAndView(
    viewId(JOURNAL_ENTRY_VIEW_NAME, fragment), mapOf(
        "client" to clientRef,
        "entryId" to entry.id,
        "entry" to EditJournalEntryRq(entry),
        "duplicatedDate" to duplicatedDate,
        "formAction" to formAction
    )
)

fun editFormAction(clientId: ClientRef, entryId: Long) = "/therapist/clients/${clientId.id}/journal/$entryId"
