package pro.qyoga.app.therapist.clients.journal.edit_entry.create

import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.viewId
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.app.therapist.clients.journal.edit_entry.create.CreateJournalEntryPageController.Companion.CREATE_JOURNAL_PAGE_URL
import pro.qyoga.app.therapist.clients.journal.edit_entry.shared.JOURNAL_ENTRY_VIEW_NAME
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.model.ClientRef
import java.time.LocalDate


data class CreateJournalEntryPageModel(
    private val client: Client,
    private val entryDate: LocalDate,
    private val fragment: String? = null,
    private val duplicatedDate: Boolean = false
) : ModelAndView(
    viewId(JOURNAL_ENTRY_VIEW_NAME, fragment), mapOf(
        "client" to client,
        "entryDate" to entryDate,
        "duplicatedDate" to duplicatedDate,
        "formAction" to createFormAction(client.ref())
    )
)

fun createFormAction(clientRef: ClientRef) = CREATE_JOURNAL_PAGE_URL.replace("{clientId}", clientRef.id.toString())
