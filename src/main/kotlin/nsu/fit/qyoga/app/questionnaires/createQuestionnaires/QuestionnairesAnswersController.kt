package nsu.fit.qyoga.app.questionnaires.createQuestionnaires

import jakarta.servlet.http.HttpSession
import nsu.fit.qyoga.core.questionnaires.api.dtos.*
import nsu.fit.qyoga.core.questionnaires.api.dtos.extensions.*
import nsu.fit.qyoga.core.questionnaires.api.errors.ElementNotFound
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionnaireException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/therapist/questionnaires")
class QuestionnairesAnswersController(
    private val httpSession: HttpSession
) {

    private val baseQuestionErrorText = "Выбранный вопрос не найден"
    private val baseAnswerErrorText = "Выбранный ответ не найден"
    private val questionHtmlFragment = "fragments/create-questionnaire-answer::question"

    /**
     * Обновление ответа
     */
    @PostMapping("/edit/question/{questionId}/answer/{answerId}/update")
    @ResponseBody
    fun changeAnswerTitle(
        @ModelAttribute("questionnaire") questionnaireDto: CreateQuestionnaireDto,
        @PathVariable answerId: Long,
        @PathVariable questionId: Long
    ): HttpStatus {
        val questionnaire = getQuestionnaireFromSession()
        val changedAnswer = questionnaireDto.getAnswerByIdOrNull(questionId, answerId)
            ?: throw ElementNotFound(baseAnswerErrorText)
        val updatedQuestionnaire = questionnaire.updateAnswer(questionId, answerId, changedAnswer)
        setQuestionnaireInSession(updatedQuestionnaire)
        return HttpStatus.OK
    }

    /**
     * Получить фрагмент страницы задания баллов для ответов
     */
    @PostMapping("/edit/question/{questionId}/answer/setScores")
    fun setQuestionScore(
        @PathVariable("questionId") questionId: Long,
        @ModelAttribute("questionnaire") questionnaireDto: CreateQuestionnaireDto,
        model: Model
    ): String {
        updateQuestion(getQuestionnaireFromSession(), questionnaireDto, questionId, model)
        return "fragments/create-questionnaire-answer-set-score::answersScore"
    }

    /**
     * Получить фрагмент страницы редактирования ответа
     */
    @PostMapping("/edit/question/{questionId}/answer/setAnswers")
    fun setQuestionAnswers(
        @PathVariable("questionId") questionId: Long,
        @ModelAttribute("questionnaire") questionnaireDto: CreateQuestionnaireDto,
        model: Model
    ): String {
        updateQuestion(
            getQuestionnaireFromSession(),
            questionnaireDto,
            questionId,
            model
        )
        return questionHtmlFragment
    }

    /**
     * Добавление ответа в вопрос
     */
    @GetMapping("/edit/question/{questionId}/addAnswer")
    fun addAnswerToQuestion(
        @PathVariable questionId: Long,
        model: Model
    ): String {
        val questionnaire = getQuestionnaireFromSession()
        val question = questionnaire.getQuestionByIdOrNull(questionId)
            ?: throw ElementNotFound(baseQuestionErrorText)
        val updatedQuestionnaire = questionnaire.addAnswer(question, questionId)
        setQuestionnaireInSession(updatedQuestionnaire)
        model.addAllAttributes(setQuestionIdxAndQuestion(updatedQuestionnaire, questionId))
        return questionHtmlFragment
    }

    /**
     * Удаление ответа из вопроса
     */
    @DeleteMapping("/edit/question/{questionId}/answer/{answerId}")
    fun deleteAnswerFromQuestion(
        @PathVariable answerId: Long,
        @PathVariable questionId: Long,
        model: Model
    ): String {
        val questionnaire = getQuestionnaireFromSession()
        val question = questionnaire.getQuestionByIdOrNull(questionId)
            ?: throw ElementNotFound(baseQuestionErrorText)
        val updatedQuestionnaire = questionnaire.deleteAnswer(question, questionId, answerId)
        setQuestionnaireInSession(updatedQuestionnaire)
        model.addAllAttributes(setQuestionIdxAndQuestion(updatedQuestionnaire, questionId))
        return questionHtmlFragment
    }

    fun updateQuestion(
        questionnaire: CreateQuestionnaireDto,
        changedQuestionnaireDto: CreateQuestionnaireDto,
        questionId: Long,
        model: Model
    ) {
        val question = changedQuestionnaireDto.getQuestionByIdOrNull(questionId)
            ?: throw ElementNotFound(baseQuestionErrorText)
        val updatedQuestionnaire = questionnaire.updateQuestionAnswers(question, questionId)
        setQuestionnaireInSession(updatedQuestionnaire)
        model.addAllAttributes(setQuestionIdxAndQuestion(updatedQuestionnaire, questionId))
    }

    fun setQuestionIdxAndQuestion(
        questionnaire: CreateQuestionnaireDto,
        questionId: Long
    ): Map<String, *> {
        val questionIndex = questionnaire.getQuestionIdxById(questionId)
        return mapOf(
            "questionIndex" to questionIndex,
            "question" to questionnaire.getQuestionByIdOrNull(questionId)
        )
    }

    fun getQuestionnaireFromSession(): CreateQuestionnaireDto {
        return httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw QuestionnaireException("Ошибка извлечения опросника из сессии")
    }

    fun setQuestionnaireInSession(questionnaire: CreateQuestionnaireDto) {
        httpSession.setAttribute("questionnaire", questionnaire)
    }
}
