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

const val QUESTION_FRAGMENT = "fragments/create-questionnaire-answer::question"

@Controller
@RequestMapping("/therapist/questionnaires")
class QuestionnairesAnswersController(
    private val httpSession: HttpSession,
    private val imageService: ImageService
) {
    val questionIndexFieldName = "questionIndex"
    val questionFieldName = "question"
    val questionnaireFieldName = "questionnaire"

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
        val questionList = questionnaire.question.mapIndexed { qIdx, question ->
            if (question.id == questionId) {
                model.addAttribute("questionIndex", qIdx)
                question.copy(
                    answers = question.answers.mapIndexed { aIdx, answer ->
                        if (answer.id == answerId) {
                            model.addAttribute("answerIndex", aIdx)
                            val copiedAnswer = answer.copy(
                                imageId = imageService.uploadImage(
                                    Image(
                                        name = file.name,
                                        mediaType = file.contentType.toString(),
                                        size = file.size,
                                        data = file.bytes
                                    )
                                )
                            )
                            model.addAttribute("answer", copiedAnswer)
                            model.addAttribute("questionId", question.id)
                            copiedAnswer
                        } else {
                            answer
                        }
                    }
                )
            } else {
                question
            }
        }.toMutableList()
        httpSession.setAttribute(
            questionnaireFieldName,
            questionnaire.copy(question = questionList)
        )
        return "fragments/create-questionnaire-image::answerImage"
    }

    /**
     * Удаление изображение из ответа
     */
    @DeleteMapping("/edit/question/{questionId}/answer/{answerId}/image")
    fun deleteImageFromAnswer(
        @PathVariable questionId: Long,
        @PathVariable answerId: Long
    ): ResponseEntity<String> {
        val questionnaire = getQuestionnaireFromSession()
        val questionList = questionnaire.question.map { question ->
            if (question.id == questionId) {
                question.copy(
                    answers = question.answers.map { answer ->
                        if (answer.id == answerId) {
                            imageService.deleteImage(answer.imageId!!)
                            answer.copy(imageId = null)
                        } else {
                            answer
                        }
                    }
                )
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
     * Добавление ответа в вопрос
     */
    @GetMapping("/edit/question/{questionId}/addAnswer")
    fun addAnswerToQuestion(
        @PathVariable questionId: Long,
        model: Model
    ): String {
        val questionnaire = getQuestionnaireFromSession()
        val changedQuestion = getQuestionById(questionnaire, questionId)
        val lastId = if (changedQuestion.answers.isEmpty()) 0 else changedQuestion.answers.last().id + 1
        val questionList = questionnaire.question.mapIndexed { idx, value ->
            if (value.id == questionId) {
                model.addAttribute(questionIndexFieldName, idx)
                val copiedQuestion = changedQuestion.copy(
                    answers = changedQuestion.answers + CreateAnswerDto(id = lastId)
                )
                model.addAttribute(questionFieldName, copiedQuestion)
                copiedQuestion
            } else {
                value
            }
        }.toMutableList()
        httpSession.setAttribute(
            questionnaireFieldName,
            questionnaire.copy(question = questionList)
        )
        return QUESTION_FRAGMENT
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
        val changedQuestion = getQuestionById(questionnaire, questionId)
        val questionList = questionnaire.question.mapIndexed { idx, value ->
            if (value.id == questionId) {
                model.addAttribute(questionIndexFieldName, idx)
                val copiedQuestion = changedQuestion.copy(
                    answers = changedQuestion.answers.filter { it.id != answerId }
                )
                model.addAttribute(questionFieldName, copiedQuestion)
                copiedQuestion
            } else {
                value
            }
        }.toMutableList()
        httpSession.setAttribute(
            questionnaireFieldName,
            questionnaire.copy(question = questionList)
        )
        return QUESTION_FRAGMENT
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
        val questionList = questionnaire.question.map { value ->
            if (value.id == questionId) {
                val copiedQuestion = changedQuestion.copy(
                    answers = changedQuestion.answers.map {
                        if (it.id == answerId) changedAnswer else it
                    }
                )
                copiedQuestion
            } else {
                value
            }
        }.toMutableList()
        httpSession.setAttribute(
            questionnaireFieldName,
            questionnaire.copy(question = questionList)
        )
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
        val questionnaire = getQuestionnaireFromSession()
        httpSession.setAttribute(
            questionnaireFieldName,
            updateQuestion(questionnaire, questionnaireDto, questionId, model)
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
        val questionnaire = getQuestionnaireFromSession()
        httpSession.setAttribute(
            questionnaireFieldName,
            updateQuestion(questionnaire, questionnaireDto, questionId, model)
        )
        return QUESTION_FRAGMENT
    }

    fun updateQuestion(
        questionnaire: CreateQuestionnaireDto,
        questionnaireDto: CreateQuestionnaireDto,
        questionId: Long,
        model: Model
    ): CreateQuestionnaireDto {
        val changedQuestion = getQuestionById(questionnaireDto, questionId)
        val questionList = questionnaire.question.mapIndexed { idx, value ->
            if (value.id == questionId) {
                model.addAttribute(questionIndexFieldName, idx)
                val copiedQuestion = changedQuestion.copy(answers = changedQuestion.answers)
                model.addAttribute(questionFieldName, copiedQuestion)
                copiedQuestion
            } else {
                value
            }
        }.toMutableList()
        return questionnaire.copy(
            question = questionList
        )
    }

    fun getQuestionnaireFromSession(): CreateQuestionnaireDto {
        return httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw QuestionnaireException("Ошибка извлечения опросника из сессии")
    }

    fun getQuestionById(questionnaire: CreateQuestionnaireDto, questionId: Long): CreateQuestionDto {
        return questionnaire.question.filter { it.id == questionId }.getOrNull(0)
            ?: throw ElementNotFound("Выбранный вопрос не найден")
    }
}
