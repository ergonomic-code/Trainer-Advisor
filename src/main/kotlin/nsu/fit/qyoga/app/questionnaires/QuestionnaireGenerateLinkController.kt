package nsu.fit.qyoga.app.questionnaires

import nsu.fit.qyoga.core.clients.api.ClientService
import nsu.fit.qyoga.core.clients.api.Dto.ClientDto
import nsu.fit.qyoga.core.clients.api.Dto.ClientSearchDto
import nsu.fit.qyoga.core.questionnaires.internal.CompletingLinkGenerator
import nsu.fit.qyoga.core.users.internal.QyogaUserDetails
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/therapist/questionnaires")
class QuestionnaireGenerateLinkController(
    private val clientService: ClientService,
    private val completingLinkGenerator: CompletingLinkGenerator
) {

    /**
     * Получение модального окна генерации ссылки на прохождение
     */
    @GetMapping("/generate-link/{questionnaireId}")
    fun getGenerateLinkModalWindow(
        @ModelAttribute("searchDto") searchDto: ClientSearchDto,
        @PageableDefault(value = 5, page = 0) pageable: Pageable,
        @PathVariable questionnaireId: Long,
        model: Model
    ): String {
        val clients = clientService.getClients(
            searchDto,
            pageable
        )
        model.addAllAttributes(toModelAttributes(clients, searchDto, questionnaireId))
        return "questionnaire/generate_link_modal"
    }

    /**
     * Действмя с клиентами
     */
    @GetMapping("/generate-link/{questionnaireId}/action")
    fun getClientsFiltered(
        @ModelAttribute("searchDto") searchDto: ClientSearchDto,
        @PageableDefault(value = 5, page = 0) pageable: Pageable,
        @PathVariable questionnaireId: Long,
        model: Model
    ): String {
        val clients = clientService.getClients(
            searchDto,
            pageable
        )
        model.addAllAttributes(toModelAttributes(clients, searchDto, questionnaireId))
        return "questionnaire/generate_link_modal :: clients"
    }

    /**
     * Генерация ссылки на прохождение
     */
    @GetMapping("/generate-link/{questionnaireId}/{clientId}/generate")
    fun generateLink(
        @PathVariable questionnaireId: Long,
        @PathVariable clientId: Long,
        @AuthenticationPrincipal principal: QyogaUserDetails,
        model: Model
    ): String {
        model.addAttribute(
            "generatedLink",
            completingLinkGenerator.generateCompletingLink(questionnaireId, clientId, principal.id)
        )
        return "questionnaire/generate_link_modal :: questionnaire-url"
    }

    fun toModelAttributes(
        clients: Page<ClientDto>,
        searchDto: ClientSearchDto,
        questionnaireId: Long
    ): Map<String, *> = mapOf(
        "searchDto" to searchDto,
        "clients" to clients,
        "questionnaireId" to questionnaireId
    )
}
