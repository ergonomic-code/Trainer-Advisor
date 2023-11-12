package pro.qyoga.app.therapist.clients

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import pro.qyoga.core.clients.api.ClientDto
import pro.qyoga.core.clients.api.ClientSearchDto
import pro.qyoga.core.clients.api.ClientsCrudService

@Controller
@RequestMapping("/therapist/clients")
class ClientListPageController(
    private val clientsCrudService: ClientsCrudService
) {
    @GetMapping
    fun getClients(
        @PageableDefault(value = 10, page = 0) pageable: Pageable,
        model: Model
    ): String {
        val clients = clientsCrudService.findClients(ClientSearchDto.ALL, pageable)
        model.addAllAttributes(toModelAttributes(clients, ClientSearchDto.ALL))
        return "therapist/clients/clients-list"
    }

    @GetMapping("/search")
    fun getClientsFiltered(
        searchDto: ClientSearchDto,
        @PageableDefault(value = 10, page = 0) pageable: Pageable,
        model: Model
    ): String {
        val clients = clientsCrudService.findClients(searchDto, pageable)
        model.addAllAttributes(toModelAttributes(clients, searchDto))
        return "therapist/clients/clients-list :: clients"
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    fun deleteClient(@PathVariable id: Long) {
        clientsCrudService.deleteById(id)
    }

    fun toModelAttributes(clients: Page<ClientDto>, searchDto: ClientSearchDto): Map<String, *> =
        mapOf(
            "searchDto" to searchDto,
            "clients" to clients,
            "pageNumbers" to 1..clients.totalPages
        )
}
