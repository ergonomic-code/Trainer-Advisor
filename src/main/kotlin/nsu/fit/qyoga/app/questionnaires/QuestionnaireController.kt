package nsu.fit.qyoga.app.questionnaires

import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/therapist/questionnaires")
class QuestionnaireController(
    private val questionnaireService: QuestionnaireService
) {

    /**
     * Получение опросника
     */
    @GetMapping("/{questionnaireId}")
    fun getQuestionnaire(
        model: Model,
        @PathVariable questionnaireId: Long
    ): String {
        model.addAttribute(
            "questionnaire",
            questionnaireService.findQuestionnaireWithQuestions(questionnaireId)
        )
        return "questionnaire/questionnaire"
    }

    /**
     * Получение результатов опросника
     */
    @GetMapping("/{questionnaireId}/decoding")
    fun getQuestionnaireResults(
        model: Model,
        @PathVariable questionnaireId: Long
    ): String {
        model.addAttribute(
            "questionnaire",
            questionnaireService.findQuestionnaireWithQuestions(questionnaireId)
        )
        return "questionnaire/decoding"
    }
}
