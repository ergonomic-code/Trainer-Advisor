package nsu.fit.qyoga.app.questionnaires

import jakarta.servlet.http.HttpSession
import nsu.fit.qyoga.core.questionnaires.api.dtos.*
import nsu.fit.qyoga.core.questionnaires.api.services.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/therapist/questionnaires")
class QuestionnairesController(
    private val questionnaireService: QuestionnaireService,
    private val httpSession: HttpSession
) {

    /**
     * Получение списка опросников
     */
    @GetMapping()
    fun getQuestionnairesList(
        @ModelAttribute("questionnaireSearchDto") questionnaireSearchDto: QuestionnaireSearchDto,
        @PageableDefault(value = 10, page = 0, sort = ["title"]) pageable: Pageable,
        model: Model
    ): String {
        val questionnaires = questionnaireService.findQuestionnaires(
            questionnaireSearchDto,
            pageable
        )
        addQuestionnairePageAttributes(model, questionnaireSearchDto, questionnaires)
        return "questionnaire/questionnaire-list"
    }

    /**
     * Фильтрация опросников
     */
    @GetMapping("action")
    fun sortQuestionnaires(
        @ModelAttribute("questionnaireSearchDto") questionnaireSearchDto: QuestionnaireSearchDto,
        @PageableDefault(value = 10, page = 0, sort = ["title"]) pageable: Pageable,
        model: Model
    ): String {
        val questionnaires = questionnaireService.findQuestionnaires(
            questionnaireSearchDto,
            pageable
        )
        addQuestionnairePageAttributes(model, questionnaireSearchDto, questionnaires)
        return "questionnaire/questionnaire-list :: page-content"
    }

    /**
     * Получение опросника
     */
    @GetMapping("/{questionnaireId}")
    fun getQuestionnaire(
        model: Model,
        @PathVariable questionnaireId: Long
    ): String {
        model.addAttribute("questionnaire", getQuestionnaire(questionnaireId))
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
        return "questionnaire/decoding"
    }

    /**
     * Действия с опросником
     */
    @GetMapping("/{questionnaireId}/action")
    fun getQuestionnaireAction(
        model: Model,
        @PathVariable questionnaireId: Long
    ): String {
        model.addAttribute("questionnaire", getQuestionnaire(questionnaireId))
        return "questionnaire/modal_window"
    }

    fun getQuestionnaire(questionnaireId: Long): CreateQuestionnaireDto? {
        val inSessionQuestionnaire = httpSession.getAttribute("watchQuestionnaire") as CreateQuestionnaireDto?
        return if (inSessionQuestionnaire == null || inSessionQuestionnaire.id != questionnaireId) {
            val tempQ = questionnaireService.findQuestionnaireWithQuestions(questionnaireId)
            httpSession.setAttribute("watchQuestionnaire", tempQ)
            tempQ
        } else {
            inSessionQuestionnaire
        }
    }

    fun addQuestionnairePageAttributes(
        model: Model,
        questionnaireSearchDto: QuestionnaireSearchDto,
        questionnaires: Page<QuestionnaireDto>
    ) {
        model.addAttribute("questionnaires", questionnaires)
        model.addAttribute("questionnaireSearchDto", questionnaireSearchDto)
        model.addAttribute(
            "sortType",
            questionnaires.sort.getOrderFor("title").toString().substringAfter(' ')
        )
    }
}
