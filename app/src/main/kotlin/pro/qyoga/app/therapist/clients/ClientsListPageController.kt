package pro.qyoga.app.therapist.clients

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import pro.azhidkov.platform.spring.sdj.withSortBy
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.cards.dtos.ClientSearchDto
import pro.qyoga.core.clients.cards.findTherapistClientsPageBySearchForm
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import java.util.*

@Controller
class ClientsListPageController(
    private val clientsRepo: ClientsRepo
) {

    @GetMapping
    @RequestMapping(PATH)
    fun getClients(
        @AuthenticationPrincipal principal: QyogaUserDetails,
        @PageableDefault(value = 10, page = 0) pageRequest: Pageable,
    ): ClientsListPageModel {
        val searchDto = ClientSearchDto.ALL
        val clients =
            clientsRepo.findTherapistClientsPageBySearchForm(
                therapistId = principal.id,
                searchDto,
                pageRequest.withSortBy(Client::lastName)
            )
        return ClientsListPageModel(clients, searchDto)
    }

    @GetMapping(SEARCH_PATH)
    fun getClientsFiltered(
        @AuthenticationPrincipal principal: QyogaUserDetails,
        searchDto: ClientSearchDto,
        @PageableDefault(value = 10, page = 0) pageRequest: Pageable,
    ): ClientsListPageModel {
        val clients =
            clientsRepo.findTherapistClientsPageBySearchForm(
                therapistId = principal.id,
                searchDto,
                pageRequest.withSortBy(Client::lastName)
            )
        return ClientsListPageModel(clients, searchDto, "clients")
    }

    @DeleteMapping(DELETE_PATH)
    @ResponseBody
    fun deleteClient(@PathVariable id: UUID) {
        clientsRepo.deleteById(id)
    }

    companion object {

        const val PATH = "/therapist/clients"
        const val SEARCH_PATH = "/therapist/clients/search"
        const val DELETE_PATH = "/therapist/clients/delete/{id}"

        const val SEARCH_PARAM_PAGE = "page"
        const val SEARCH_PARAM_FIRST_NAME = "firstName"
        const val SEARCH_PARAM_LAST_NAME = "lastName"
        const val SEARCH_PARAM_PHONE_NUMBER = "phoneNumber"

        const val SEARCH_URL =
            "$SEARCH_PATH?$SEARCH_PARAM_FIRST_NAME={firstName}&$SEARCH_PARAM_LAST_NAME={lastName}&$SEARCH_PARAM_PHONE_NUMBER={phoneNumber}&$SEARCH_PARAM_PAGE={page}"

    }

}