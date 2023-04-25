package nsu.fit.qyoga.app.questionnaires

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireWithQuestionDto
import nsu.fit.qyoga.core.questionnaires.api.services.AnswerService
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionService
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*


@Controller
@RequestMapping("/questionnaires/")
class QuestionnairesCreateController(
    private val questionnaireService: QuestionnaireService,
    private val questionService: QuestionService,
    private val answerService: AnswerService
) {

    /**
     * Создание нового опросника
     */
    @GetMapping("new")
    fun getCreateQuestionnairePage(): String {
        val questionnaireId = questionnaireService.createQuestionnaire()
        val question = questionService.createQuestion(questionnaireId)
        answerService.createAnswer(question.id)
        return "redirect:/questionnaires/$questionnaireId/edit"
    }

    /**
     * Редактирование опросника
     */
    @GetMapping("{id}/edit")
    fun editQuestionnaire(
        model: Model,
        @PathVariable id: Long
    ): String {
        setQuestionnaireInModel(id, model)
        return "questionnaire/create-questionnaire"
    }

    /**
     * Создание опросника
     */
    @PostMapping("{id}/edit")
    fun createQuestionnaire(
        @ModelAttribute("questionnaire") questionnaire: QuestionnaireWithQuestionDto,
        @PathVariable id: Long
    ): String {
        questionnaireService.updateQuestionnaire(questionnaire)
        println("Hi")
        return "redirect:/questionnaires/$id/setResult"
    }

    /**
     * Задание заголовка опросника
     */
    @PostMapping("{id}/edit/title")
    @ResponseBody
    fun changeQuestionnaireTitle(
        questionnaire: QuestionnaireDto
    ): HttpStatus {
        questionnaireService.updateQuestionnaire(questionnaire)
        return HttpStatus.OK
    }

    fun setQuestionnaireInModel(
        questionnaireId: Long,
        model: Model
    ) {
        model.addAttribute(
            "questionnaire",
            questionnaireService.findQuestionnaireWithQuestions(questionnaireId)
        )
    }
}
