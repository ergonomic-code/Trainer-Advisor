package pro.qyoga.app.therapist.clients

import org.springframework.data.domain.Page
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.viewId
import pro.qyoga.core.clients.cards.dtos.ClientSearchDto
import pro.qyoga.core.clients.cards.model.Client


data class ClientsListPageModel(
    val clients: Page<Client>,
    val searchDto: ClientSearchDto,
    val fragment: String? = null
) : ModelAndView(
    viewId("therapist/clients/clients-list", fragment), mapOf(
        "searchDto" to searchDto,
        "clients" to clients,
        "pageNumbers" to 1..clients.totalPages
    )
)