package nsu.fit.qyoga.app.questionnaires

import jakarta.servlet.http.HttpSession
import nsu.fit.qyoga.core.images.api.ImageService
import nsu.fit.qyoga.core.images.api.model.Image
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateAnswerDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto
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
class QuestionnairesQuestionController(
    private val httpSession: HttpSession,
    private val imageService: ImageService
) {
    val questionnaireFieldName = "questionnaire"

    /***
     * Добавление нового вопроса
     */
    @GetMapping("/edit/add-question")
    fun addNewQuestionToQuestionnaire(): String {
        val questionnaire = getQuestionnaireFromSession()
        val lastId = if (questionnaire.question.isEmpty()) 0 else questionnaire.question.last().id + 1
        val newQuestion = CreateQuestionDto(id = lastId, answers = listOf(CreateAnswerDto()))
        httpSession.setAttribute(
            questionnaireFieldName,
            questionnaire.copy(question = (questionnaire.question + newQuestion).toMutableList())
        )
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
        val questionnaire = getQuestionnaireFromSession()
        val questionList = questionnaire.question.mapIndexed { qIdx, question ->
            if (question.id == questionId) {
                model.addAttribute("questionIndex", qIdx)
                val copiedQuestion = question.copy(
                    imageId = imageService.uploadImage(
                        Image(
                            name = file.name,
                            mediaType = file.contentType.toString(),
                            size = file.size,
                            data = file.bytes
                        )
                    )
                )
                model.addAttribute("question", copiedQuestion)
                copiedQuestion
            } else {
                question
            }
        }.toMutableList()
        httpSession.setAttribute(
            questionnaireFieldName,
            questionnaire.copy(question = questionList)
        )
        return "fragments/create-questionnaire-image::questionImage"
    }

    /**
     * Удаление изображение из вопроса
     */
    @DeleteMapping("/edit/question/{questionId}/image")
    fun deleteImageFromQuestion(
        @PathVariable questionId: Long
    ): ResponseEntity<String> {
        val questionnaire = getQuestionnaireFromSession()
        val questionList = questionnaire.question.map { question ->
            if (question.id == questionId) {
                imageService.deleteImage(question.imageId!!)
                val copiedQuestion = question.copy(imageId = null)
                copiedQuestion
            } else {
                question
            }
        }.toMutableList()
        httpSession.setAttribute(
            questionnaireFieldName,
            questionnaire.copy(question = questionList)
        )
        return ResponseEntity.ok().body("")
    }

    /**
     * Удаление вопроса из опросника
     */
    @DeleteMapping("/edit/question/{questionId}")
    fun deleteQuestionFromQuestionnaire(
        @PathVariable questionId: Long
    ): ResponseEntity<String> {
        val questionnaire = getQuestionnaireFromSession()
        httpSession.setAttribute(
            questionnaireFieldName,
            questionnaire.copy(question = questionnaire.question.filter { it.id != questionId }.toMutableList())
        )
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
            questionnaireFieldName,
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
        @ModelAttribute("questionnaires") questionnaireDto: CreateQuestionnaireDto,
        @PathVariable questionId: Long
    ): HttpStatus {
        val questionnaire = getQuestionnaireFromSession()
        val changedQuestion = getQuestionById(questionnaireDto, questionId)
            ?: throw ElementNotFound("Выбранный вопрос не найден")
        val questionList = questionnaire.question.map {
            if (it.id == questionId) {
                it.copy(
                    title = changedQuestion.title,
                    questionType = changedQuestion.questionType,
                    answers = changedQuestion.answers
                )
            } else {
                it
            }
        }.toMutableList()
        httpSession.setAttribute(
            questionnaireFieldName,
            questionnaire.copy(
                question = questionList
            )
        )
        return HttpStatus.OK
    }

    fun getQuestionnaireFromSession(): CreateQuestionnaireDto {
        return httpSession.getAttribute(questionnaireFieldName) as CreateQuestionnaireDto?
            ?: throw QuestionnaireException("Ошибка извлечения опросника из сессии")
    }

    fun getQuestionById(questionnaire: CreateQuestionnaireDto, questionId: Long): CreateQuestionDto? {
        return questionnaire.question.filter { it.id == questionId }.getOrNull(0)
    }
}
