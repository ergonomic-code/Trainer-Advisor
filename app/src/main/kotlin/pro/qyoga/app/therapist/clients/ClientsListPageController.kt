package pro.qyoga.app.therapist.clients

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import pro.azhidkov.platform.spring.sdj.withSortBy
import pro.qyoga.app.therapist.clients.ClientsListPageController.Companion.PATH
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.cards.dtos.ClientSearchDto
import pro.qyoga.core.clients.cards.findTherapistClientsPageBySearchForm
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import java.util.*

private const val CLIENTS = "clients"

@Controller
@RequestMapping(PATH)
class ClientsListPageController(
    private val clientsRepo: ClientsRepo
) {

    @GetMapping
    fun getClients(
        @AuthenticationPrincipal principal: QyogaUserDetails,
        @PageableDefault(value = 10, page = 0) pageRequest: Pageable,
        model: Model
    ): String {
        val searchDto = ClientSearchDto.ALL
        val clients =
            clientsRepo.findTherapistClientsPageBySearchForm(
                therapistId = principal.id,
                searchDto,
                pageRequest.withSortBy(Client::lastName)
            )
        model.addAllAttributes(toModelAttributes(clients, searchDto))
        return "therapist/clients/clients-list"
    }

    @GetMapping(SEARCH_SEGMNET)
    fun getClientsFiltered(
        @AuthenticationPrincipal principal: QyogaUserDetails,
        searchDto: ClientSearchDto,
        @PageableDefault(value = 10, page = 0) pageRequest: Pageable,
        model: Model
    ): String {
        val clients =
            clientsRepo.findTherapistClientsPageBySearchForm(
                therapistId = principal.id,
                searchDto,
                pageRequest.withSortBy(Client::lastName)
            )
        model.addAllAttributes(toModelAttributes(clients, searchDto))
        return "therapist/clients/clients-list :: clients"
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    fun deleteClient(@PathVariable id: UUID) {
        clientsRepo.deleteById(id)
    }

    fun toModelAttributes(clients: Page<Client>, searchDto: ClientSearchDto): Map<String, *> =
        mapOf(
            "searchDto" to searchDto,
            CLIENTS to clients,
            "pageNumbers" to 1..clients.totalPages
        )

    companion object {

        const val PATH = "/therapist/clients"
        const val SEARCH_SEGMNET = "search"
        const val SEARCH_PATH = "$PATH/$SEARCH_SEGMNET"

        const val SEARCH_PARAM_PAGE = "page"
        const val SEARCH_PARAM_FIRST_NAME = "firstName"
        const val SEARCH_PARAM_LAST_NAME = "lastName"
        const val SEARCH_PARAM_PHONE_NUMBER = "phoneNumber"

        const val SEARCH_URL =
            "$SEARCH_PATH?$SEARCH_PARAM_FIRST_NAME={firstName}&$SEARCH_PARAM_LAST_NAME={lastName}&$SEARCH_PARAM_PHONE_NUMBER={phoneNumber}&$SEARCH_PARAM_PAGE={page}"

        @Suppress("UNCHECKED_CAST")
        fun getClients(model: Model) = model.getAttribute(CLIENTS) as Page<Client>

    }

}