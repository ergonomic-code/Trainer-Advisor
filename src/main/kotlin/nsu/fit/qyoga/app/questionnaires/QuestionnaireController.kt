package nsu.fit.qyoga.app.questionnaires

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireSearchDto
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

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

    /**
     * Получение списка опросников
     */
    @GetMapping("/{questionnaireId}/delete")
    fun deleteQuestionnaire(
        @PathVariable questionnaireId: Long,
        @ModelAttribute("questionnaireSearchDto") questionnaireSearchDto: QuestionnaireSearchDto,
        @PageableDefault(value = 10, page = 0, sort = ["title"]) pageable: Pageable,
        model: Model
    ): String {
        questionnaireService.deleteQuestionnaireById(questionnaireId)
        val questionnaires = questionnaireService.findQuestionnaires(
            questionnaireSearchDto,
            pageable
        )
        model.addAttribute("questionnaires", questionnaires)
        model.addAttribute("questionnaireSearchDto", questionnaireSearchDto)
        model.addAttribute(
            "sortType",
            questionnaires.sort.getOrderFor("title").toString().substringAfter(' ')
        )
        return "questionnaire/questionnaire-list :: page-content"
    }
}
