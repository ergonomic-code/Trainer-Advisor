package nsu.fit.qyoga.app.completingQuestionnaires

import nsu.fit.qyoga.core.completingQuestionnaires.api.services.CompletingService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/questionnaires/")
class QuestionnaireSingleController(
    private val completingService: CompletingService
) {

    /**
     * Просмотр опросника
     */
    @GetMapping("{id}")
    fun getQuestionnaireById(
        @PathVariable id: Long,
        model: Model
    ): String {
        model.addAttribute("results", completingService.findCompletingByQId(id))
        return "completingQuestionnaires/questionnaire-page"
    }
}