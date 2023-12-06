package pro.qyoga.app.therapist.clients.journal

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.qyoga.core.clients.cards.api.ClientsService
import pro.qyoga.core.users.internal.QyogaUserDetails
import pro.qyoga.platform.spring.mvc.modelAndView

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

        return modelAndView("therapist/clients/journal-create") {
            "client" bindTo client
        }
    }

    @PostMapping("/create")
    fun createJournalEntry(
        @PathVariable clientId: Long,
        @ModelAttribute createJournalEntryRequest: CreateJournalEntryRequest,
        @AuthenticationPrincipal principal: QyogaUserDetails,
    ): ModelAndView {
        val result = createJournalEntryWorkflow.createJournalEntry(clientId, createJournalEntryRequest, principal)
        if (result == CreateJournalEntryResult.ClientNotFound) {
            return ModelAndView("forward:error/404")
        }

        return ModelAndView("redirect:/therapist/clients/$clientId/journal")
    }

}