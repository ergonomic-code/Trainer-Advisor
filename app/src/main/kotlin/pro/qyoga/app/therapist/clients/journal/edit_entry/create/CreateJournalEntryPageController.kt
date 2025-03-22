package pro.qyoga.app.therapist.clients.journal.edit_entry.create

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.kotlin.isFailureOf
import pro.azhidkov.platform.spring.http.hxRedirect
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.app.platform.notFound
import pro.qyoga.app.therapist.clients.journal.edit_entry.edit.EditJournalEntryPageModel
import pro.qyoga.app.therapist.clients.journal.edit_entry.shared.JOURNAL_ENTRY_FROM
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.journals.dtos.EditJournalEntryRq
import pro.qyoga.core.clients.journals.errors.DuplicatedDate
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import java.time.LocalDate
import java.util.*


@Controller
class CreateJournalEntryPageController(
    private val clientsRepo: ClientsRepo,
    private val createJournalEntry: CreateJournalEntryOp
) {

    @GetMapping(CREATE_JOURNAL_PAGE_URL)
    fun handleGetCreateJournalEntryPage(
        @PathVariable clientId: UUID
    ): ModelAndView {
        val client = clientsRepo.findByIdOrNull(clientId)
            ?: return notFound

        return CreateJournalEntryPageModel(
            client.ref(),
            LocalDate.now(),
        )
    }

    @PostMapping(CREATE_JOURNAL_PAGE_URL)
    fun handleCreateJournalEntry(
        @PathVariable clientId: UUID,
        @ModelAttribute editJournalEntryRq: EditJournalEntryRq,
        @AuthenticationPrincipal principal: QyogaUserDetails,
    ): Any {
        val result = runCatching {
            createJournalEntry.createJournalEntry(clientId, editJournalEntryRq, principal)
        }

        return when {
            result.isSuccess ->
                hxRedirect("/therapist/clients/$clientId/journal")

            result.isFailureOf<DuplicatedDate>() -> {
                val ex = result.exceptionOrNull() as DuplicatedDate
                EditJournalEntryPageModel(
                    ex.duplicatedEntry.clientRef,
                    ex.duplicatedEntry,
                    createFormAction(ex.duplicatedEntry.clientRef),
                    JOURNAL_ENTRY_FROM,
                    true,
                )
            }

            else ->
                result.getOrThrow()
        }
    }

    companion object {
        const val CREATE_JOURNAL_PAGE_URL = "/therapist/clients/{clientId}/journal/create"
    }

}