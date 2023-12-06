package pro.qyoga.app.therapist.clients.cards

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
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
    ): String {
        val client = clientsService.findClient(id)
            ?: return "forward:error/404"

        model.addAllAttributes(
            mapOf(
                "client" to client,
                "formAction" to "/therapist/clients/${client.id}/card",
                "activeTab" to "card"
            )
        )

        return "therapist/clients/client-edit"
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