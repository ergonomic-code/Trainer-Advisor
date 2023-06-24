package nsu.fit.qyoga.app.questionnaires.createQuestionnaires

import jakarta.servlet.http.HttpSession
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.extensions.*
import nsu.fit.qyoga.core.questionnaires.api.dtos.getQuestionByIdOrNull
import nsu.fit.qyoga.core.questionnaires.api.dtos.getQuestionIdxById
import nsu.fit.qyoga.core.questionnaires.api.errors.ElementNotFound
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionnaireException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/therapist/questionnaires")
class QuestionnairesQuestionController(
    private val httpSession: HttpSession
) {

    private val baseQuestionErrorText = "Выбранный вопрос не найден"

    /***
     * Добавление нового вопроса
     */
    @GetMapping("/edit/add-question")
    fun addNewQuestionToQuestionnaire(): String {
        val questionnaire = getQuestionnaireFromSession()
        setQuestionnaireInSession(questionnaire.addQuestion())
        return "questionnaire/create-questionnaire::questions"
    }

    /**
     * Удаление вопроса из опросника
     */
    @DeleteMapping("/edit/question/{questionId}")
    fun deleteQuestionFromQuestionnaire(
        @PathVariable questionId: Long
    ): ResponseEntity<String> {
        val questionnaire = getQuestionnaireFromSession()
        setQuestionnaireInSession(questionnaire.deleteQuestion(questionId))
        return ResponseEntity.ok().body("")
    }

    /**
     * Изменить тип вопроса
     */
    @PostMapping("/edit/question/{questionId}/change-type")
    fun changeAnswersType(
        @PathVariable questionId: Long,
        @ModelAttribute("questionnaires") questionnaireDto: CreateQuestionnaireDto,
        model: Model
    ): String {
        val questionnaire = getQuestionnaireFromSession()
        val changedQuestion = questionnaireDto.getQuestionByIdOrNull(questionId)
            ?: throw ElementNotFound(baseQuestionErrorText)
        val updatedQuestionnaire = questionnaire.changeQuestionType(questionId, changedQuestion)
        setQuestionnaireInSession(updatedQuestionnaire)
        val questionIndex = questionnaire.getQuestionIdxById(questionId)
        model.addAllAttributes(
            mapOf(
                "questionIndex" to questionIndex,
                "question" to updatedQuestionnaire.getQuestionByIdOrNull(questionId)
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
        @ModelAttribute("questionnaires") questionnaireDto: CreateQuestionnaireDto,
        @PathVariable questionId: Long
    ): HttpStatus {
        val questionnaire = getQuestionnaireFromSession()
        val changedQuestion = questionnaireDto.getQuestionByIdOrNull(questionId)
            ?: throw ElementNotFound(baseQuestionErrorText)
        val updatedQuestionnaire = questionnaire.updateQuestion(questionId, changedQuestion)
            ?: throw ElementNotFound(baseQuestionErrorText)
        setQuestionnaireInSession(updatedQuestionnaire)
        return HttpStatus.OK
    }

    fun getQuestionnaireFromSession(): CreateQuestionnaireDto {
        return httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw QuestionnaireException("Ошибка извлечения опросника из сессии")
    }

    fun setQuestionnaireInSession(questionnaire: CreateQuestionnaireDto) {
        httpSession.setAttribute("questionnaire", questionnaire)
    }
}
