package pro.qyoga.app.therapist.clients.journal.edit_entry.edit

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.http.hxRedirect
import pro.qyoga.app.platform.notFound
import pro.qyoga.app.therapist.clients.journal.edit_entry.shared.JOURNAL_ENTRY_FROM
import pro.qyoga.core.clients.cards.model.ClientRef
import pro.qyoga.core.clients.journals.JournalEntriesRepo
import pro.qyoga.core.clients.journals.dtos.EditJournalEntryRq
import pro.qyoga.core.clients.journals.errors.DuplicatedDate
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import java.util.*


@Controller
class EditJournalEntryPageController(
    private val journalsEntriesRepo: JournalEntriesRepo,
    private val getJournalEntry: GetJournalEntryOp,
    private val editJournalEntry: EditJournalEntryOp,
) {

    @GetMapping(PATH)
    fun handleGetEditJournalEntryPage(
        @PathVariable clientId: UUID,
        @PathVariable entryId: Long
    ): ModelAndView {
        val result = getJournalEntry.getJournalEntry(clientId, entryId)
            ?: return notFound

        return EditJournalEntryPageModel(
            result.clientRef,
            result,
            formAction = editFormAction(result.clientRef, result.id),
        )
    }

    @PostMapping(PATH)
    fun handleEditJournalEntry(
        @PathVariable clientId: UUID,
        @PathVariable entryId: Long,
        @ModelAttribute editJournalEntryRq: EditJournalEntryRq,
        @AuthenticationPrincipal principal: QyogaUserDetails,
    ): Any {
        try {
            editJournalEntry(ClientRef.to(clientId), entryId, editJournalEntryRq, principal)
            return hxRedirect("/therapist/clients/$clientId/journal")
        } catch (ex: DuplicatedDate) {
            return EditJournalEntryPageModel(
                ex.duplicatedEntry.clientRef,
                ex.duplicatedEntry,
                editFormAction(ex.duplicatedEntry.clientRef, ex.duplicatedEntry.id),
                fragment = JOURNAL_ENTRY_FROM,
                duplicatedDate = true,
            )
        }
    }

    @DeleteMapping(PATH)
    @ResponseStatus(HttpStatus.OK)
    fun handleDeleteJournalEntry(
        @PathVariable clientId: UUID,
        @PathVariable entryId: Long,
    ) {
        journalsEntriesRepo.deleteById(entryId)
    }

    companion object {
        const val PATH = "/therapist/clients/{clientId}/journal/{entryId}"
    }

}