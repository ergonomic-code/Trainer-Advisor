package nsu.fit.qyoga.app.questionnaires

import jakarta.servlet.http.HttpSession
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionException
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

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
                id = getLastQuestionIndex(questionnaire.question) + 1
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
        @PathVariable questionId: Long
    ): String {
        return "questionnaire/create-questionnaire::questions"
    }

    /**
     * Обновить вопрос
     */
    /*@PostMapping("{questionnaireId}/edit/question/{questionId}/update")
    @ResponseBody
    fun changeQuestionTitle(
        @ModelAttribute("questionnaire") questionnaire: CreateQuestionnaireDto,
        @PathVariable questionId: Long,
        @PathVariable questionnaireId: Long
    ): HttpStatus {
        throw QuestionException("Выбранный вопрос не найден")
    }*/

    /*fun returnQuestionsPage(
        questionnaireId: Long,
        model: Model
    ): String {
        model.addAttribute(
            "questionnaire",
            questionnaireService.findQuestionnaireWithQuestions(questionnaireId)
        )
        return "questionnaire/create-questionnaire::questions"
    }

    fun setQuestionWithId(
        question: CreateQuestionDto,
        questionIndex: Int,
        model: Model
    ) {
        model.addAttribute("question", question)
        model.addAttribute("questionIndex", questionIndex)
    }*/

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
