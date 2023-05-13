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
@RequestMapping("/therapist/questionnaires")
class QuestionnairesAnswersController(
    private val httpSession: HttpSession
) {

    /**
     * Добавление изображение ответу
     */
    /*@PostMapping("answer/{id}/image")
    fun addImageToAnswer(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("questionIndex") questionIndex: Int,
        @RequestParam("answerIndex") answerIndex: Int,
        @PathVariable id: Long,
        model: Model
    ): String {
        val answer = answerService.findAnswer(id)
        answer.imageId?.let {
            imageService.deleteImage(it)
        }
        val createAnswerDto = CreateAnswerDto(
            id = answer.id,
            title = answer.title,
            bounds = AnswerBoundsDto(
                lowerBound = answer.bounds.lowerBound,
                lowerBoundText = answer.bounds.lowerBoundText,
                upperBound = answer.bounds.upperBound,
                upperBoundText = answer.bounds.upperBoundText
            ),
            score = answer.score,
            imageId = imageService.uploadImage(file)
        )
        answerService.updateAnswer(createAnswerDto)
        model.addAttribute("answer", createAnswerDto)
        model.addAttribute("questionIndex", questionIndex)
        model.addAttribute("answerIndex", answerIndex)
        return "fragments/create-questionnaire-image::answerImage"
    }*/

    /**
     * Добавление ответа в вопрос
     */
    @GetMapping("/edit/question/{questionId}/addAnswer")
    fun addAnswerToQuestion(
        @PathVariable questionId: Long,
        model: Model
    ): String {
        val questionnaire = httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw ElementNotFound("Невозможно добавить новый ответ")
        val changedQuestion = getQuestionById(questionnaire, questionId)
            ?: throw ElementNotFound("Выбранный вопрос не найден")
        val lastId = if (changedQuestion.answers.isEmpty()) 0 else changedQuestion.answers.last().id + 1
        val questionList = questionnaire.question.mapIndexed { idx, value ->
            if (value.id == questionId) {
                model.addAttribute("questionIndex", idx)
                val copiedQuestion = changedQuestion.copy(
                    answers = changedQuestion.answers + CreateAnswerDto(id = lastId)
                )
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
     * Удаление ответа из вопроса
     */
    @DeleteMapping("/edit/question/{questionId}/answer/{answerId}")
    fun deleteAnswerFromQuestion(
        @PathVariable answerId: Long,
        @PathVariable questionId: Long,
        model: Model
    ): String {
        val questionnaire = httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw ElementNotFound("Невозможно удалить ответ")
        val changedQuestion = getQuestionById(questionnaire, questionId)
            ?: throw ElementNotFound("Выбранный вопрос не найден")
        val questionList = questionnaire.question.mapIndexed { idx, value ->
            if (value.id == questionId) {
                model.addAttribute("questionIndex", idx)
                val copiedQuestion = changedQuestion.copy(
                    answers = changedQuestion.answers.filter { it.id != answerId }
                )
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
     * Обновление ответа
     */
    @PostMapping("/edit/question/{questionId}/answer/{answerId}/update")
    @ResponseBody
    fun changeAnswerTitle(
        @ModelAttribute("questionnaire") questionnaireDto: CreateQuestionnaireDto,
        @PathVariable answerId: Long,
        @PathVariable questionId: Long
    ): HttpStatus {
        val questionnaire = httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw ElementNotFound("Невозможно сохранить ответ")
        httpSession.setAttribute(
            "questionnaire",
            updateAnswer(questionnaire, questionnaireDto, answerId, questionId)
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
        val questionnaire = httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw ElementNotFound("Невозможно сохранить ответ")
        httpSession.setAttribute(
            "questionnaire",
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
        val questionnaire = httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
            ?: throw ElementNotFound("Невозможно сохранить ответ")
        httpSession.setAttribute(
            "questionnaire",
            updateQuestion(questionnaire, questionnaireDto, questionId, model)
        )
        return "fragments/create-questionnaire-answer::question"
    }

    fun updateAnswer(
        questionnaire: CreateQuestionnaireDto,
        questionnaireDto: CreateQuestionnaireDto,
        answerId: Long,
        questionId: Long
    ): CreateQuestionnaireDto {
        val changedQuestion = getQuestionById(questionnaireDto, questionId)
            ?: throw ElementNotFound("Выбранный вопрос не найден")
        val changedAnswer = getAnswerById(changedQuestion, answerId)
            ?: throw ElementNotFound("Выбранный ответ не найден")
        val questionList = questionnaire.question.mapIndexed { idx, value ->
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
        return questionnaire.copy(
            question = questionList
        )
    }

    fun updateQuestion(
        questionnaire: CreateQuestionnaireDto,
        questionnaireDto: CreateQuestionnaireDto,
        questionId: Long,
        model: Model
    ): CreateQuestionnaireDto {
        val changedQuestion = getQuestionById(questionnaireDto, questionId)
            ?: throw ElementNotFound("Выбранный вопрос не найден")
        val questionList = questionnaire.question.mapIndexed { idx, value ->
            if (value.id == questionId) {
                model.addAttribute("questionIndex", idx)
                val copiedQuestion = changedQuestion.copy(answers = changedQuestion.answers)
                model.addAttribute("question", copiedQuestion)
                copiedQuestion
            } else {
                value
            }
        }.toMutableList()
        return questionnaire.copy(
            question = questionList
        )
    }

    fun getAnswerById(changedQuestion: CreateQuestionDto, answerId: Long): CreateAnswerDto? {
        return changedQuestion.answers.filter { it.id == answerId }.getOrNull(0)
    }

    fun getQuestionById( questionnaire: CreateQuestionnaireDto, questionId: Long): CreateQuestionDto? {
        return questionnaire.question.filter { it.id == questionId }.getOrNull(0)
    }
}
