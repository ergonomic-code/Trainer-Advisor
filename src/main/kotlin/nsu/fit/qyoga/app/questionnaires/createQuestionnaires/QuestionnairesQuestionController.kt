package nsu.fit.qyoga.app.questionnaires.createQuestionnaires

import jakarta.servlet.http.HttpSession
import nsu.fit.platform.lang.getQuestionnaireFromSession
import nsu.fit.platform.lang.setQuestionnaireInSession
import nsu.fit.qyoga.core.images.api.ImageService
import nsu.fit.qyoga.core.images.api.model.Image
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.extensions.*
import nsu.fit.qyoga.core.questionnaires.api.dtos.getQuestionByIdOrNull
import nsu.fit.qyoga.core.questionnaires.api.dtos.getQuestionIdxById
import nsu.fit.qyoga.core.questionnaires.api.errors.ElementNotFound
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/therapist/questionnaires")
class QuestionnairesQuestionController(
    private val httpSession: HttpSession,
    private val imageService: ImageService
) {

    private val baseQuestionErrorText = "Выбранный вопрос не найден"

    /***
     * Добавление нового вопроса
     */
    @GetMapping("/edit/add-question")
    fun addNewQuestionToQuestionnaire(): String {
        val questionnaire = httpSession.getQuestionnaireFromSession()
        httpSession.setQuestionnaireInSession(questionnaire.addQuestion())
        return "questionnaire/create-questionnaire::questions"
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
        val questionnaire = httpSession.getQuestionnaireFromSession()
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
        httpSession.setQuestionnaireInSession(updatedQuestionnaire)
        model.addAllAttributes(setQuestionIdxAndQuestion(updatedQuestionnaire, questionId))
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
        val questionnaire = httpSession.getQuestionnaireFromSession()
        imageService.deleteImage(imageId)
        val updatedQuestionnaire = questionnaire.deleteQuestionImage(questionId)
            ?: throw ElementNotFound(baseQuestionErrorText)
        httpSession.setQuestionnaireInSession(updatedQuestionnaire)
        return ResponseEntity.ok().body("")
    }

    /**
     * Удаление вопроса из опросника
     */
    @DeleteMapping("/edit/question/{questionId}")
    fun deleteQuestionFromQuestionnaire(
        @PathVariable questionId: Long
    ): ResponseEntity<String> {
        val questionnaire = httpSession.getQuestionnaireFromSession()
        httpSession.setQuestionnaireInSession(questionnaire.deleteQuestion(questionId))
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
        val questionnaire = httpSession.getQuestionnaireFromSession()
        val changedQuestion = questionnaireDto.getQuestionByIdOrNull(questionId)
            ?: throw ElementNotFound(baseQuestionErrorText)
        val updatedQuestionnaire = questionnaire.changeQuestionType(questionId, changedQuestion)
        httpSession.setQuestionnaireInSession(updatedQuestionnaire)
        model.addAllAttributes(setQuestionIdxAndQuestion(updatedQuestionnaire, questionId))
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
        val questionnaire = httpSession.getQuestionnaireFromSession()
        val changedQuestion = questionnaireDto.getQuestionByIdOrNull(questionId)
            ?: throw ElementNotFound(baseQuestionErrorText)
        val updatedQuestionnaire = questionnaire.updateQuestion(questionId, changedQuestion)
            ?: throw ElementNotFound(baseQuestionErrorText)
        httpSession.setQuestionnaireInSession(updatedQuestionnaire)
        return HttpStatus.OK
    }

    fun setQuestionIdxAndQuestion(
        questionnaire: CreateQuestionnaireDto,
        questionId: Long
    ): Map<String, *> {
        val questionIndex = questionnaire.getQuestionIdxById(questionId)
        return mapOf(
            "questionIndex" to questionIndex,
            "question" to questionnaire.question.first { it.id == questionId }
        )
    }
}
