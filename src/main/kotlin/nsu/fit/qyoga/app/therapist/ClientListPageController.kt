package nsu.fit.qyoga.app.therapist

import nsu.fit.qyoga.core.clients.api.ClientService
import nsu.fit.qyoga.core.clients.api.Dto.ClientDto
import nsu.fit.qyoga.core.clients.api.Dto.ClientSearchDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors
import java.util.stream.IntStream

const val CLIENT_PAGE = "clients-list"

@Controller
@RequestMapping("/clients")
class ClientListPageController(
    private val clientService: ClientService
) {
    @GetMapping
    fun getClients(
        @ModelAttribute("searchDto") searchDto: ClientSearchDto,
        @RequestParam(value = "pageSize", required = false, defaultValue = "10") pageSize: Int,
        @RequestParam(value = "pageNumber", required = false, defaultValue = "1") pageNumber: Int,
        model: Model
    ): String {
        val clients = clientService.getClients(
            searchDto,
            PageRequest.of(pageNumber - 1, pageSize)
        )
        addClientsPageAttributes(model, clients)
        return CLIENT_PAGE
    }

    @GetMapping("/search-cl")
    fun getClientsFiltered(
        @ModelAttribute("searchDto") searchDto: ClientSearchDto,
        @RequestParam(value = "pageSize", required = false, defaultValue = "10") pageSize: Int,
        @RequestParam(value = "pageNumber", required = false, defaultValue = "1") pageNumber: Int,
        model: Model
    ): String {
        val clients = clientService.getClients(
            searchDto,
            PageRequest.of(pageNumber - 1, pageSize)
        )
        addClientsPageAttributes(model, clients)
        return "clients-list :: clients"
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    fun deleteClient(
        @PathVariable id: Int
    ): String {
        clientService.deleteClient(id)
        return ""
    }

    fun addClientsPageAttributes(model: Model, clients: Page<ClientDto>) {
        model.addAttribute("searchDto", ClientSearchDto())
        model.addAttribute("clients", clients)
        model.addAttribute(
            "pageNumbers",
            IntStream.rangeClosed(1, clients.totalPages).boxed().collect(Collectors.toList())
        )
    }
}
