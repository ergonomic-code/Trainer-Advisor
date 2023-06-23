package nsu.fit.qyoga.app.questionnaires.createQuestionnaires

import jakarta.servlet.http.HttpSession
import nsu.fit.qyoga.core.images.api.ImageService
import nsu.fit.qyoga.core.images.api.model.Image
import nsu.fit.qyoga.core.questionnaires.api.dtos.*
import nsu.fit.qyoga.core.questionnaires.api.dtos.extensions.*
import nsu.fit.qyoga.core.questionnaires.api.errors.ElementNotFound
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionnaireException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/therapist/questionnaires")
class QuestionnairesAnswersController(
    private val httpSession: HttpSession,
    private val imageService: ImageService
) {

    /**
     * Добавление изображение ответу
     */
    @PostMapping("/edit/question/{questionId}/answer/{answerId}/add-image")
    fun addImageToAnswer(
        @RequestParam("file") file: MultipartFile,
        @PathVariable questionId: Long,
        @PathVariable answerId: Long,
        model: Model
    ): String {
        val questionnaire = getQuestionnaireFromSession()
        val imageId = imageService.uploadImage(
            Image(
                name = file.name,
                mediaType = file.contentType.toString(),
                size = file.size,
                data = file.bytes
            )
        )
        val updatedQuestionnaire = questionnaire.addAnswerImage(questionId, answerId, imageId)
        if (updatedQuestionnaire == null) {
            imageService.deleteImage(imageId)
            throw ElementNotFound("Выбранный вопрос не найден")
        }
        setQuestionnaireInSession(updatedQuestionnaire)
        val questionIndex = updatedQuestionnaire.getQuestionIdx(questionId)
        val attributes: Map<String, *> = mapOf(
            "questionIndex" to questionIndex,
            "answer" to updatedQuestionnaire.question[questionIndex].answers.first { it.id == answerId },
            "questionId" to questionId
        )
        model.addAllAttributes(attributes)
        return "fragments/create-questionnaire-image::answerImage"
    }

    /**
     * Удаление изображение из ответа
     */
    @DeleteMapping("/edit/question/{questionId}/answer/{answerId}/image/{imageId}")
    fun deleteImageFromAnswer(
        @PathVariable questionId: Long,
        @PathVariable answerId: Long,
        @PathVariable imageId: Long
    ): ResponseEntity<String> {
        val questionnaire = getQuestionnaireFromSession()
        imageService.deleteImage(imageId)
        val updatedQuestionnaire = questionnaire.deleteAnswersImage(questionId, answerId)
            ?: throw ElementNotFound("Выбранный ответ не найден")
        setQuestionnaireInSession(updatedQuestionnaire)
        return ResponseEntity.ok().body("")
    }

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
        val changedQuestion = getQuestionById(questionnaireDto, questionId)
        val changedAnswer = changedQuestion.answers.filter { it.id == answerId }.getOrNull(0)
            ?: throw ElementNotFound("Выбранный ответ не найден")
        val updatedQuestionnaire = questionnaire.updateAnswer(questionId, answerId, changedAnswer)
            ?: throw ElementNotFound("Выбранный ответ не найден")
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
        updateQuestion(
            getQuestionnaireFromSession(),
            questionnaireDto,
            questionId,
            model
        )
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
        return "fragments/create-questionnaire-answer::question"
    }

    fun updateQuestion(
        questionnaire: CreateQuestionnaireDto,
        changedQuestionnaire: CreateQuestionnaireDto,
        questionId: Long,
        model: Model
    ) {
        val updatedQuestionnaire = questionnaire.updateQuestionAnswers(
            getQuestionById(changedQuestionnaire, questionId),
            questionId
        ) ?: throw ElementNotFound("Выбранный вопрос не найден")
        setQuestionnaireInSession(updatedQuestionnaire)
        model.addAllAttributes(setQuestionIdxAndQuestion(model, updatedQuestionnaire, questionId))
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
        val updatedQuestionnaire = questionnaire.addAnswer(
            getQuestionById(questionnaire, questionId),
            questionId
        )
        setQuestionnaireInSession(updatedQuestionnaire)
        model.addAllAttributes(setQuestionIdxAndQuestion(model, updatedQuestionnaire, questionId))
        return "fragments/create-questionnaire-answer::question"
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
        val updatedQuestionnaire = questionnaire.deleteAnswer(
            getQuestionById(questionnaire, questionId),
            questionId,
            answerId
        )
        setQuestionnaireInSession(updatedQuestionnaire)
        model.addAllAttributes(setQuestionIdxAndQuestion(model, updatedQuestionnaire, questionId))
        return "fragments/create-questionnaire-answer::question"
    }

    fun setQuestionIdxAndQuestion(
        model: Model,
        questionnaire: CreateQuestionnaireDto,
        questionId: Long
    ): Map<String, *> {
        val questionIndex = questionnaire.getQuestionIdx(questionId)
        return mapOf(
            "questionIndex" to questionIndex,
            "question" to questionnaire.question.first { it.id == questionId }
        )
    }

    fun getQuestionnaireFromSession(): CreateQuestionnaireDto {
        return httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw QuestionnaireException("Ошибка извлечения опросника из сессии")
    }

    fun setQuestionnaireInSession(questionnaire: CreateQuestionnaireDto) {
        httpSession.setAttribute("questionnaire", questionnaire)
    }

    fun getQuestionById(questionnaire: CreateQuestionnaireDto, questionId: Long): CreateQuestionDto {
        return questionnaire.question.filter { it.id == questionId }.getOrNull(0)
            ?: throw ElementNotFound("Выбранный вопрос не найден")
    }
}
