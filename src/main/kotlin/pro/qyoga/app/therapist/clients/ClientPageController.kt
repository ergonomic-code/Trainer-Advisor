package pro.qyoga.app.therapist.clients

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import pro.qyoga.core.clients.api.ClientCardDto
import pro.qyoga.core.clients.api.ClientsService
import pro.qyoga.core.users.internal.QyogaUserDetails


@Controller
@RequestMapping("/therapist/clients")
class ClientPageController(
    private val clientsService: ClientsService
) {

    private val clientPage = "therapist/clients/client"

    @GetMapping("/create")
    fun getCreateClientPage(model: Model): String {
        model.addAttribute("formAction", "/therapist/clients/create")
        return clientPage
    }

    @GetMapping("/{id}")
    fun getEditClientPage(
        @PathVariable id: Long,
        model: Model
    ): String {
        val client = clientsService.findClient(id)
            ?: return "forward:error/404"

        model.addAllAttributes(
            mapOf(
                "client" to client,
                "formAction" to "/therapist/clients/${client.id}",
            )
        )

        return clientPage
    }

    @PostMapping("/create")
    fun createClient(
        clientCardDto: ClientCardDto,
        @AuthenticationPrincipal principal: QyogaUserDetails,
    ): String {
        clientsService.createClients(principal.id, listOf(clientCardDto))
        return "redirect:/therapist/clients"
    }

    @PostMapping("/{id}")
    fun editClient(
        clientCardDto: ClientCardDto,
        @PathVariable id: Long
    ): String {
        clientsService.editClient(id, clientCardDto)
        return "redirect:/therapist/clients"
    }

}