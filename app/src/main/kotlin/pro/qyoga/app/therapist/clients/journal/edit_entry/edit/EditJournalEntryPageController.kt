package pro.qyoga.app.therapist.clients.journal.edit_entry.edit

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.http.hxRedirect
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.app.platform.notFound
import pro.qyoga.app.therapist.clients.journal.edit_entry.shared.JOURNAL_ENTRY_VIEW_NAME
import pro.qyoga.core.clients.journals.JournalEntriesRepo
import pro.qyoga.core.clients.journals.dtos.EditJournalEntryRequest
import pro.qyoga.core.clients.journals.errors.DuplicatedDate
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import java.util.*


@Controller
@RequestMapping("/therapist/clients/{clientId}/journal/{entryId}")
class EditJournalEntryPageController(
    private val journalsEntriesRepo: JournalEntriesRepo,
    private val getJournalEntry: GetJournalEntryOp,
    private val editJournalEntry: EditJournalEntryOp,
) {

    @GetMapping()
    fun handleGetEditJournalEntryPage(
        @PathVariable clientId: UUID,
        @PathVariable entryId: Long
    ): ModelAndView {
        val result = getJournalEntry.getJournalEntry(clientId, entryId)
            ?: return notFound

        return modelAndView(JOURNAL_ENTRY_VIEW_NAME) {
            "client" bindTo result.clientRef
            "entry" bindTo result
            "formAction" bindTo editFormAction(clientId, entryId)
        }
    }

    private fun editFormAction(clientId: UUID, entryId: Long) = "/therapist/clients/$clientId/journal/$entryId"

    @PostMapping
    fun handleEditJournalEntry(
        @PathVariable clientId: UUID,
        @PathVariable entryId: Long,
        @ModelAttribute editJournalEntryRequest: EditJournalEntryRequest,
        @AuthenticationPrincipal principal: QyogaUserDetails,
    ): Any {
        try {
            editJournalEntry.editJournalEntry(clientId, entryId, editJournalEntryRequest, principal)
            return hxRedirect("/therapist/clients/$clientId/journal")
        } catch (ex: DuplicatedDate) {
            return modelAndView("$JOURNAL_ENTRY_VIEW_NAME :: journalEntryFrom") {
                "client" bindTo ex.duplicatedEntry.clientRef
                "entry" bindTo ex.duplicatedEntry
                "duplicatedDate" bindTo true
                "formAction" bindTo editFormAction(clientId, entryId)
            }
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    fun handleDeleteJournalEntry(
        @PathVariable clientId: UUID,
        @PathVariable entryId: Long,
    ) {
        journalsEntriesRepo.deleteById(entryId)
    }

}