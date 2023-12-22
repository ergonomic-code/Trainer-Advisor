package pro.qyoga.app.therapist.clients.journal.create_entry

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.qyoga.core.clients.cards.api.ClientsService
import pro.qyoga.core.clients.journals.api.DuplicatedDate
import pro.qyoga.core.users.internal.QyogaUserDetails
import pro.qyoga.platform.kotlin.isFailureOf
import pro.qyoga.platform.spring.http.hxRedirect
import pro.qyoga.platform.spring.mvc.modelAndView
import java.time.LocalDate

private const val CREATE_ENTRY_VIEW_NAME = "therapist/clients/journal-create"

@Controller
@RequestMapping("/therapist/clients/{clientId}/journal")
class JournalEntryPageController(
    private val clientsService: ClientsService,
    private val createJournalEntryWorkflow: CreateJournalEntryWorkflow
) {

    @GetMapping("/create")
    fun getCreateJournalEntryPage(
        @PathVariable clientId: Long
    ): ModelAndView {
        val client = clientsService.findClient(clientId)
            ?: return ModelAndView("forward:error/404")

        return modelAndView(CREATE_ENTRY_VIEW_NAME) {
            "client" bindTo client
            "entryDate" bindTo LocalDate.now()
        }
    }

    @PostMapping("/create")
    fun createJournalEntry(
        @PathVariable clientId: Long,
        @ModelAttribute createJournalEntryRequest: CreateJournalEntryRequest,
        @AuthenticationPrincipal principal: QyogaUserDetails,
    ): Any {
        val result = runCatching {
            createJournalEntryWorkflow.createJournalEntry(clientId, createJournalEntryRequest, principal)
        }

        return when {
            result.isSuccess ->
                hxRedirect("/therapist/clients/$clientId/journal")

            result.isFailureOf<DuplicatedDate>() -> {
                val ex = result.exceptionOrNull() as DuplicatedDate
                modelAndView("$CREATE_ENTRY_VIEW_NAME :: journalEntryFrom") {
                    "client" bindTo ex.duplicatedEntry.client
                    "entry" bindTo ex.duplicatedEntry
                    "entryDate" bindTo ex.duplicatedEntry.date
                    "duplicatedDate" bindTo true
                }
            }

            else ->
                result.getOrThrow()
        }
    }

}