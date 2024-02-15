package pro.qyoga.app.therapist.clients.cards

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import pro.qyoga.app.platform.notFound
import pro.qyoga.app.therapist.clients.ClientPageTab
import pro.qyoga.app.therapist.clients.clientPageModel
import pro.qyoga.core.clients.cards.api.ClientCardDto
import pro.qyoga.core.clients.cards.api.ClientsService


@Controller
@RequestMapping("/therapist/clients/{id}/card")
class EditClientCardPageController(
    private val clientsService: ClientsService
) {

    @GetMapping
    fun getEditClientCardPage(
        @PathVariable id: Long,
        model: Model
    ): ModelAndView {
        val client = clientsService.findClient(id)
            ?: return notFound

        return clientPageModel(client, ClientPageTab.CARD) {
            "formAction" bindTo "/therapist/clients/${client.id}/card"
        }
    }

    @PostMapping
    fun editClientCard(
        clientCardDto: ClientCardDto,
        @PathVariable id: Long
    ): String {
        clientsService.editClient(id, clientCardDto)
        return "redirect:/therapist/clients"
    }

}