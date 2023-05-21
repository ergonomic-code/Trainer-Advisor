package nsu.fit.qyoga.app.completingQuestionnaires

import jakarta.servlet.http.HttpSession
import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingSearchDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.services.CompletingService
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireSearchDto
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionnaireException
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import nsu.fit.qyoga.core.users.internal.UserDetailsServiceImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal

@Controller
@RequestMapping("/therapist/questionnaires")
class QuestionnaireSingleController(
    private val completingService: CompletingService,
    private val httpSession: HttpSession,
    private val questionnaireService: QuestionnaireService,
    private val userService: UserDetailsServiceImpl
) {

    /**
     * Просмотр опросника
     */
    @GetMapping("/{id}")
    fun getQuestionnaireById(
        @ModelAttribute("completingSearchDto") completingSearchDto: CompletingSearchDto,
        @PageableDefault(value = 10, page = 0, sort = ["date"]) pageable: Pageable,
        @PathVariable id: Long,
        principal: Principal,
        model: Model
    ): String {
        val clientId = httpSession.getAttribute("clientId") as Long?
            ?: userService.findUserIdByUsername(principal.name)
        model.addAttribute("questionnaireId", id)
        model.addAttribute("questionnaireTitle", questionnaireService.getQuestionnairesTitleById(id))
        model.addAttribute(
            "results",
            completingService.findCompletingByQId(
                id,
                clientId,
                completingSearchDto,
                pageable
            )
        )
        return "completingQuestionnaires/questionnaire-page"
    }

    /**
     * Фильтрация прохождений
     */
    @GetMapping("/{id}/action")
    fun sortQuestionnaires(
        @PathVariable id: Long,
        @ModelAttribute("completingSearchDto") completingSearchDto: CompletingSearchDto,
        @PageableDefault(value = 10, page = 0, sort = ["date"]) pageable: Pageable,
        model: Model,
        principal: Principal
    ): String {
        val clientId = httpSession.getAttribute("clientId") as Long?
            ?: userService.findUserIdByUsername(principal.name)
        model.addAttribute(
            "results",
            completingService.findCompletingByQId(
                id,
                clientId,
                completingSearchDto,
                pageable
            )
        )

        return "questionnaire/questionnaire-list :: page-content"
    }
}
