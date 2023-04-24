package nsu.fit.qyoga.app.completingQuestionnaires

import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingFindDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.services.CompletingService
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import nsu.fit.qyoga.core.users.internal.UserDetailsServiceImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal

@Controller
@RequestMapping("/questionnaires/")
class QuestionnaireSingleController(
    private val completingService: CompletingService,
    private val questionnaireService: QuestionnaireService,
    private val userService: UserDetailsServiceImpl
) {

    /**
     * Просмотр опросника
     */
    @GetMapping("{id}")
    fun getQuestionnaireById(
        @PageableDefault(value = 10, page = 0, sort = ["date"]) pageable: Pageable,
        @PathVariable id: Long,
        principal: Principal,
        model: Model
    ): String {
        model.addAttribute("questionnaireTitle", questionnaireService.findQuestionnaireById(id))
        model.addAttribute(
            "results",
            completingService.findCompletingByQId(
                id,
                userService.findUserIdByUsername(principal.name),
                CompletingFindDto(""),
                pageable
            )
        )
        return "completingQuestionnaires/questionnaire-page"
    }

    /**
     * Получение модального окна для генерации ссылки на прохождение опросника
     */
    @GetMapping("{id}/generateLinkModal")
    fun getGenerateLinkModalWindow(
        @PathVariable id: Long,
        model: Model
    ): String {

        model.addAttribute("questionnaireId", id)
        return "completingQuestionnaires/generate-link-fragment"
    }
}
