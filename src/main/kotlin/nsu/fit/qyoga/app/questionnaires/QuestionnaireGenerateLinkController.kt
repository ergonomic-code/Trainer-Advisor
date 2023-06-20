package nsu.fit.qyoga.app.questionnaires

import nsu.fit.platform.lang.toHexString
import nsu.fit.qyoga.core.clients.api.ClientService
import nsu.fit.qyoga.core.clients.api.Dto.ClientDto
import nsu.fit.qyoga.core.clients.api.Dto.ClientSearchDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.services.CompletingService
import nsu.fit.qyoga.core.users.internal.QyogaUserDetails
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.HMac
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.KeyParameter
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.SHA256Digest
import org.springframework.beans.factory.annotation.Value
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
    @Value("\${qyoga.hash.secret-key}")
    private val  key: String
)  {

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

    @GetMapping("/generate-link/{questionnaireId}/{clientId}/generate")
    fun generateLink(
        @PathVariable questionnaireId: Long,
        @PathVariable clientId: Long,
        @AuthenticationPrincipal principal: QyogaUserDetails,
        model: Model
    ): String {
        val hash = generateHash("questionnaireId:{$questionnaireId}clientId{$clientId}")
        val link = """
            /client/questionnaire-completions?
            questionnaire=$questionnaireId
            &clientId=$clientId
            &hash=$hash
            """.trimIndent().replace(" ", "")
        model.addAttribute("generatedLink",link)
        return "questionnaire/generate_link_modal :: questionnaire-url"
    }

    fun toModelAttributes(clients: Page<ClientDto>, searchDto: ClientSearchDto, questionnaireId: Long): Map<String, *> = mapOf(
        "searchDto" to searchDto,
        "clients" to clients,
        "questionnaireId" to questionnaireId
    )

    fun generateHash(value: String): String {
        val hMac = HMac(SHA256Digest())
        hMac.init(KeyParameter(key.toByteArray()))
        val hmacIn = value.toByteArray()
        hMac.update(hmacIn, 0, hmacIn.size)
        val hmacOut = ByteArray(hMac.macSize)
        hMac.doFinal(hmacOut, 0)
        return hmacOut.toHexString()
    }
}