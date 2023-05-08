package nsu.fit.qyoga.app.questionnaires

import jakarta.servlet.http.HttpSession
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateAnswerDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
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
            ?: throw QuestionException("Невозможно добавить новый вопрос")
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
            ?: throw QuestionException("Невозможно удалить вопрос")
        questionnaire.question.removeIf { it.id == questionId}
        httpSession.setAttribute(
            "questionnaire",
            questionnaire
        )
        return "questionnaire/create-questionnaire::questions"
    }

    /**
     * Изменить тип вопроса
     */
    @PostMapping("/edit/question/{questionId}/change-type")
    fun changeAnswersType(
        @PathVariable questionId: Long,
        @ModelAttribute("questionnaire") questionnaireDto: CreateQuestionnaireDto
    ): String {
        val questionnaire = httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw QuestionException("Невозможно изменить тип вопроса")
        questionnaire.question.forEachIndexed { index, question ->
            if (question.id == questionId) {
                questionnaire.question[index] = question.copy(
                    questionType = questionnaireDto.question[index].questionType,
                    answers = listOf(CreateAnswerDto())
                )
                httpSession.setAttribute("questionnaire", questionnaire)
                return "questionnaire/create-questionnaire::questions"
            }
        }
        throw QuestionException("Выбранный вопрос не найден")
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
            ?: throw QuestionException("Невозможно изменить вопрос")
        questionnaire.question.forEachIndexed { index, question ->
            if (question.id == questionId) {
                questionnaire.question[index] = questionnaireDto.question[index]
                httpSession.setAttribute("questionnaire", questionnaire)
                return HttpStatus.OK
            }
        }
        throw QuestionException("Выбранный вопрос не найден")
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
