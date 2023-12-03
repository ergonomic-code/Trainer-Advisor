package pro.qyoga.app.therapist.clients

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import pro.qyoga.core.clients.api.ClientSearchDto
import pro.qyoga.core.clients.api.ClientsService
import pro.qyoga.core.clients.internal.Client
import pro.qyoga.core.users.internal.QyogaUserDetails

private const val CLIENTS = "clients"

@Controller
@RequestMapping("/therapist/clients")
class ClientListPageController(
    private val clientsService: ClientsService
) {

    @GetMapping
    fun getClients(
        @AuthenticationPrincipal principal: QyogaUserDetails,
        @PageableDefault(value = 10, page = 0) pageable: Pageable,
        model: Model
    ): String {
        val searchDto = ClientSearchDto.ALL
        val clients = clientsService.findClients(principal.id, searchDto, pageable)
        model.addAllAttributes(toModelAttributes(clients, searchDto))
        return "therapist/clients/clients-list"
    }

    @GetMapping("/search")
    fun getClientsFiltered(
        @AuthenticationPrincipal principal: QyogaUserDetails,
        searchDto: ClientSearchDto,
        @PageableDefault(value = 10, page = 0) pageable: Pageable,
        model: Model
    ): String {
        val clients = clientsService.findClients(principal.id, searchDto, pageable)
        model.addAllAttributes(toModelAttributes(clients, searchDto))
        return "therapist/clients/clients-list :: clients"
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    fun deleteClient(@PathVariable id: Long) {
        clientsService.deleteClient(id)
    }

    fun toModelAttributes(clients: Page<Client>, searchDto: ClientSearchDto): Map<String, *> =
        mapOf(
            "searchDto" to searchDto,
            CLIENTS to clients,
            "pageNumbers" to 1..clients.totalPages
        )

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun getClients(model: Model) = model.getAttribute(CLIENTS) as Page<Client>

    }
}
