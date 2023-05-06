package nsu.fit.qyoga.app.questionnaires

import jakarta.servlet.http.HttpSession
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.testDto
import nsu.fit.qyoga.core.questionnaires.api.model.Questionnaire
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/questionnaires")
class QuestionnairesCreateController(
    private val questionnaireService: QuestionnaireService,
    private val httpSession: HttpSession
) {

    /**
     * Создание нового опросника
     */
    @GetMapping("/new")
    fun getCreateQuestionnairePage(): String {
        val questionnaire = httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
        if(questionnaire == null || questionnaire.id != 0L) {
            httpSession.setAttribute(
                "questionnaire",
                CreateQuestionnaireDto(
                    id = 0,
                    title = ""
                )
            )
        }
        return "questionnaire/create-questionnaire"
    }

    /**
     * Редактирование опросника
      */
    @GetMapping("/{id}/edit")
    fun editQuestionnaire(
        @PathVariable id: Long
    ): String {
        httpSession.setAttribute(
            "questionnaire",
            questionnaireService.findQuestionnaireWithQuestions(id)
        )
        return "questionnaire/create-questionnaire"
    }

    /**
     * Создание опросника
     */
    @PostMapping("/edit")
    fun createQuestionnaire(
        @ModelAttribute("questionnaire") questionnaire: CreateQuestionnaireDto,
    ): String {
        httpSession.removeAttribute("questionnaire")
        questionnaireService.saveQuestionnaire(questionnaire)
        return "redirect:/questionnaires/setResult"
    }


    /**
     * Задание заголовка опросника
     */
    @PostMapping("/edit/title")
    @ResponseBody
    fun changeQuestionnaireTitle(
        @RequestParam title: String
    ): HttpStatus {
        val questionnaire = httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto
        httpSession.setAttribute("questionnaire", questionnaire.copy(title = title))
        return HttpStatus.OK
    }
}
