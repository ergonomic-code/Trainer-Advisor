package nsu.fit.qyoga.app.questionnaires

import jakarta.servlet.http.HttpSession
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateAnswerDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.errors.ElementNotFound
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/questionnaires")
class QuestionnairesQuestionController(
    private val httpSession: HttpSession
) {

    /***
     * Добавление нового вопроса
     */
    @GetMapping("/edit/add-question")
    fun addNewQuestionToQuestionnaire(): String {
        val questionnaire = httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw ElementNotFound("Невозможно добавить новый вопрос")
        questionnaire.question.add(
            CreateQuestionDto(
                id = getLastQuestionIndex(questionnaire.question) + 1,
                answers = listOf(CreateAnswerDto())
            )
        )
        httpSession.setAttribute(
            "questionnaire",
            questionnaire
        )
        return "questionnaire/create-questionnaire::questions"
    }

    /**
     * Добавление изображение вопросу
     */
    /*@PostMapping("question/{id}/image")
    fun addImageToQuestion(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("questionIndex") questionIndex: Int,
        @PathVariable id: Long,
        model: Model
    ): String {
        return "fragments/create-questionnaire-image::questionImage"
    }*/

    /**
     * Удаление вопроса из опросника
     */
    @DeleteMapping("/edit/question/{questionId}")
    fun deleteQuestionFromQuestionnaire(
        @PathVariable questionId: Long
    ): String {
        val questionnaire = httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw ElementNotFound("Невозможно удалить вопрос")
        httpSession.setAttribute(
            "questionnaire",
            questionnaire.copy(question = questionnaire.question.filter { it.id != questionId }.toMutableList())
        )
        return "questionnaire/create-questionnaire::questions"
    }

    /**
     * Изменить тип вопроса
     */
    @PostMapping("/edit/question/{questionId}/change-type")
    fun changeAnswersType(
        @PathVariable questionId: Long,
        @ModelAttribute("questionnaire") questionnaireDto: CreateQuestionnaireDto,
        model: Model
    ): String {
        val questionnaire = httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw ElementNotFound("Невозможно изменить тип вопроса")
        val question = getQuestionById(questionnaireDto, questionId)
            ?: throw ElementNotFound("Выбранный вопрос не найден")
        val questionList = questionnaire.question.mapIndexed { idx, value ->
            if (value.id == questionId) {
                model.addAttribute("questionIndex", idx)
                val copiedQuestion = question.copy(answers = listOf(CreateAnswerDto()))
                model.addAttribute("question", copiedQuestion)
                copiedQuestion
            } else {
                value
            }
        }.toMutableList()
        httpSession.setAttribute(
            "questionnaire",
            questionnaire.copy(
                question = questionList
            )
        )
        return "fragments/create-questionnaire-answer::question"
    }

    /**
     * Обновить вопрос
     */
    @PostMapping("/edit/question/{questionId}/update")
    @ResponseBody
    fun changeQuestionTitle(
        @ModelAttribute("questionnaire") questionnaireDto: CreateQuestionnaireDto,
        @PathVariable questionId: Long
    ): HttpStatus {
        val questionnaire = httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw ElementNotFound("Невозможно изменить вопрос")
        val changedQuestion = getQuestionById(questionnaireDto, questionId)
            ?: throw ElementNotFound("Выбранный вопрос не найден")
        val questionList = questionnaire.question.map {
            if (it.id == questionId) {
                changedQuestion
            } else {
                it
            }
        }.toMutableList()
        httpSession.setAttribute(
            "questionnaire",
            questionnaire.copy(
                question = questionList
            )
        )
        return HttpStatus.OK
    }

    fun getQuestionById( questionnaire: CreateQuestionnaireDto, questionId: Long): CreateQuestionDto? {
        return questionnaire.question.filter { it.id == questionId }.getOrNull(0)
    }

    fun getLastQuestionIndex (questions: List<CreateQuestionDto>): Long {
        var index = 0L
        for (question in questions) {
            if (question.id > index) {
                index=question.id
            }
        }
        return index
    }
}
