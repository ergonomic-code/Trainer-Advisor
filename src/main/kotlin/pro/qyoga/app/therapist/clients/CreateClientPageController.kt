package pro.qyoga.app.therapist.clients

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import pro.qyoga.core.clients.api.ClientsService
import pro.qyoga.core.clients.api.CreateClientRequest
import pro.qyoga.core.users.internal.QyogaUserDetails

@Controller
@RequestMapping("/therapist/clients/create")
class CreateClientPageController(
    private val clientsService: ClientsService
) {

    @GetMapping
    fun getCreateClientPage(): String {
        return "therapist/clients/clients-create"
    }

    @PostMapping
    fun createClient(
        createClientRequest: CreateClientRequest,
        @AuthenticationPrincipal principal: QyogaUserDetails,
    ): String {
        clientsService.createClients(principal.id, listOf(createClientRequest))
        return "redirect:/therapist/clients"
    }

}