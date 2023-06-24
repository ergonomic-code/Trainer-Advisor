package nsu.fit.qyoga.app.questionnaires.createQuestionnaires

import jakarta.servlet.http.HttpSession
import nsu.fit.qyoga.core.images.api.ImageService
import nsu.fit.qyoga.core.images.api.model.Image
import nsu.fit.qyoga.core.questionnaires.api.dtos.*
import nsu.fit.qyoga.core.questionnaires.api.dtos.extensions.addAnswerImage
import nsu.fit.qyoga.core.questionnaires.api.dtos.extensions.addQuestionImage
import nsu.fit.qyoga.core.questionnaires.api.dtos.extensions.deleteAnswersImage
import nsu.fit.qyoga.core.questionnaires.api.dtos.extensions.deleteQuestionImage
import nsu.fit.qyoga.core.questionnaires.api.errors.ElementNotFound
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionnaireException
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/therapist/questionnaires")
class QuestionnaireImageController(
    private val httpSession: HttpSession,
    private val imageService: ImageService
) {

    private val baseQuestionErrorText = "Выбранный вопрос не найден"
    private val baseAnswerErrorText = "Выбранный ответ не найден"

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
            throw ElementNotFound(baseAnswerErrorText)
        }
        setQuestionnaireInSession(updatedQuestionnaire)
        val attributes: Map<String, *> = mapOf(
            "questionIndex" to updatedQuestionnaire.getQuestionIdxById(questionId),
            "answerIndex" to updatedQuestionnaire.getAnswerIdxById(questionId, answerId),
            "answer" to updatedQuestionnaire.getAnswerByIdOrNull(questionId, answerId),
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
            ?: throw ElementNotFound(baseAnswerErrorText)
        setQuestionnaireInSession(updatedQuestionnaire)
        return ResponseEntity.ok().body("")
    }

    /**
     * Добавление изображение вопросу
     */
    @PostMapping("/edit/question/{questionId}/add-image")
    fun addImageToQuestion(
        @RequestParam("file") file: MultipartFile,
        @PathVariable questionId: Long,
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
        val updatedQuestionnaire = questionnaire.addQuestionImage(questionId, imageId)
        if (updatedQuestionnaire == null) {
            imageService.deleteImage(imageId)
            throw ElementNotFound(baseQuestionErrorText)
        }
        setQuestionnaireInSession(updatedQuestionnaire)
        val questionIndex = questionnaire.getQuestionIdxById(questionId)
        model.addAllAttributes(
            mapOf(
                "questionIndex" to questionIndex,
                "question" to updatedQuestionnaire.getQuestionByIdOrNull(questionId)
            )
        )
        return "fragments/create-questionnaire-image::questionImage"
    }

    /**
     * Удаление изображение из вопроса
     */
    @DeleteMapping("/edit/question/{questionId}/image/{imageId}")
    fun deleteImageFromQuestion(
        @PathVariable questionId: Long,
        @PathVariable imageId: Long
    ): ResponseEntity<String> {
        val questionnaire = getQuestionnaireFromSession()
        imageService.deleteImage(imageId)
        val updatedQuestionnaire = questionnaire.deleteQuestionImage(questionId)
            ?: throw ElementNotFound(baseQuestionErrorText)
        setQuestionnaireInSession(updatedQuestionnaire)
        return ResponseEntity.ok().body("")
    }

    fun getQuestionnaireFromSession(): CreateQuestionnaireDto {
        return httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw QuestionnaireException("Ошибка извлечения опросника из сессии")
    }

    fun setQuestionnaireInSession(questionnaire: CreateQuestionnaireDto) {
        httpSession.setAttribute("questionnaire", questionnaire)
    }
}