package pro.qyoga.app.therapist.clients.journal.edit_entry.create

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.kotlin.isFailureOf
import pro.azhidkov.platform.spring.http.hxRedirect
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.app.platform.notFound
import pro.qyoga.app.therapist.clients.journal.edit_entry.shared.JOURNAL_ENTRY_VIEW_NAME
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.journals.dtos.EditJournalEntryRequest
import pro.qyoga.core.clients.journals.errors.DuplicatedDate
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import java.time.LocalDate


@Controller
@RequestMapping("/therapist/clients/{clientId}/journal")
class CreateJournalEntryPageController(
    private val clientsRepo: ClientsRepo,
    private val createJournalEntry: CreateJournalEntryOp
) {

    @GetMapping("/create")
    fun handleGetCreateJournalEntryPage(
        @PathVariable clientId: Long
    ): ModelAndView {
        val client = clientsRepo.findByIdOrNull(clientId)
            ?: return notFound

        return modelAndView(JOURNAL_ENTRY_VIEW_NAME) {
            "client" bindTo client
            "entryDate" bindTo LocalDate.now()
            "formAction" bindTo createFormAction(clientId)
        }
    }


    @PostMapping("/create")
    fun handleCreateJournalEntry(
        @PathVariable clientId: Long,
        @ModelAttribute editJournalEntryRequest: EditJournalEntryRequest,
        @AuthenticationPrincipal principal: QyogaUserDetails,
    ): Any {
        val result = runCatching {
            createJournalEntry.createJournalEntry(clientId, editJournalEntryRequest, principal)
        }

        return when {
            result.isSuccess ->
                hxRedirect("/therapist/clients/$clientId/journal")

            result.isFailureOf<DuplicatedDate>() -> {
                val ex = result.exceptionOrNull() as DuplicatedDate
                modelAndView("$JOURNAL_ENTRY_VIEW_NAME :: journalEntryFrom") {
                    "client" bindTo ex.duplicatedEntry.client
                    "entry" bindTo ex.duplicatedEntry
                    "duplicatedDate" bindTo true
                    "formAction" bindTo createFormAction(clientId)
                }
            }

            else ->
                result.getOrThrow()
        }
    }

    private fun createFormAction(clientId: Long) = "/therapist/clients/$clientId/journal/create"

}