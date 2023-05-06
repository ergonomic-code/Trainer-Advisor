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

        httpSession.setAttribute(
            "questionnaire",
            CreateQuestionnaireDto(
                id = 0,
                title = ""
            )
        )
        val param = httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto
        println("created object with params: id = ${param.id}, title = ${param.title}")

        return "questionnaire/create-questionnaire"
    }

    /**
     * Редактирование опросника
      */
    /*@GetMapping("/{id}/edit")
    fun editQuestionnaire(
        model: Model,
        @PathVariable id: Long
    ): String {

        return "questionnaire/create-questionnaire"
    }*/

    /**
     * Создание опросника
     */
    /*@PostMapping("/{id}/edit")
    fun createQuestionnaire(
        @ModelAttribute("questionnaire") questionnaire: CreateQuestionnaireDto,
        @PathVariable id: Long
    ): String {

        return "redirect:/questionnaires/$id/setResult"
    }*/


    /**
     * Задание заголовка опросника
     */
    /*@PostMapping("/{id}/edit/title")
    @ResponseBody
    fun changeQuestionnaireTitle(
        questionnaire: QuestionnaireDto
    ): HttpStatus {

        return HttpStatus.OK
    }*/

    /*fun setQuestionnaireInModel(
        questionnaireId: Long,
        model: Model
    ) {
        model.addAttribute(
            "questionnaire",
            questionnaireService.findQuestionnaireWithQuestions(questionnaireId)
        )
    }*/
}
