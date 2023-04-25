package nsu.fit.qyoga.app.questionnaires

import nsu.fit.qyoga.core.questionnaires.api.dtos.AnswerBoundsDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.AnswerDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionWithAnswersDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireWithQuestionDto
import nsu.fit.qyoga.core.questionnaires.api.errors.AnswerException
import nsu.fit.qyoga.core.questionnaires.api.services.AnswerService
import nsu.fit.qyoga.core.questionnaires.api.services.ImageService
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionService
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/questionnaires/")
class QuestionnairesAnswersController(
    private val questionnaireService: QuestionnaireService,
    private val questionService: QuestionService,
    private val answerService: AnswerService,
    private val imageService: ImageService
) {

    /**
     * Добавление изображение ответу
     */
    @PostMapping("answer/{id}/image")
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
        val answerDto = AnswerDto(
            id = answer.id,
            title = answer.title,
            bounds = AnswerBoundsDto(
                lowerBound = answer.bounds.lowerBound,
                lowerBoundText = answer.bounds.lowerBoundText,
                upperBound = answer.bounds.upperBound,
                upperBoundText = answer.bounds.upperBoundText
            ),
            score = answer.score,
            imageId = imageService.uploadImage(file),
            questionId = answer.questionId
        )
        answerService.updateAnswer(answerDto)
        model.addAttribute("answer", answerDto)
        model.addAttribute("questionIndex", questionIndex)
        model.addAttribute("answerIndex", answerIndex)
        return "fragments/create-questionnaire-image::answerImage"
    }

    /**
     * Добавление ответа в вопрос
     */
    @GetMapping("{questionnaireId}/edit/question/{questionId}/addAnswer")
    fun addAnswerToQuestion(
        @PathVariable questionId: Long,
        @PathVariable questionnaireId: Long,
        model: Model
    ): String {
        answerService.createAnswer(questionId)
        return returnQuestionsPage(questionnaireId, model)
    }

    /**
     * Удаление ответа из вопроса
     */
    @DeleteMapping("{questionnaireId}/edit/answer/{answerId}")
    fun deleteAnswerFromQuestion(
        @PathVariable answerId: Long,
        @PathVariable questionnaireId: Long,
        model: Model
    ): String {
        answerService.deleteAnswerById(answerId)
        return returnQuestionsPage(questionnaireId, model)
    }

    /**
     * Обновление ответа
     */
    @PostMapping("question/{questionId}/answer/{answerId}/update")
    @ResponseBody
    fun changeAnswerTitle(
        @ModelAttribute("questionnaire") questionnaire: QuestionnaireWithQuestionDto,
        @PathVariable answerId: Long,
        @PathVariable questionId: Long
    ): HttpStatus {
        questionnaire.questions.forEach { question ->
            question.answers.filter { question.id == questionId }.forEach { answer ->
                if (answer.id == answerId) {
                    answerService.updateAnswer(answer)
                    return HttpStatus.OK
                }
            }
        }
        throw AnswerException("Выбранного ответа не существует")
    }

    /**
     * Получить фрагмент страницы задания баллов для ответов
     */
    @PostMapping("question/{id}/setScores")
    fun setQuestionScore(
        model: Model,
        @PathVariable id: Long,
        @ModelAttribute("questionIndex") questionIndex: Int,
        @ModelAttribute("questionnaire") questionnaire: QuestionnaireWithQuestionDto
    ): String {
        questionnaire.questions.forEach { question ->
            if (question.id == id) {
                for (answer in question.answers) {
                    answerService.updateAnswer(answer)
                }
                return@forEach
            }
        }
        val question = questionService.findQuestionWithAnswers(id)
        setQuestionWithId(question, questionIndex, model)
        return "fragments/create-questionnaire-answer-set-score::answersScore"
    }

    /**
     * Получить фрагмент страницы редактирования ответа
     */
    @PostMapping("question/{id}/setAnswers")
    fun setQuestionAnswers(
        model: Model,
        @PathVariable id: Long,
        @ModelAttribute("questionIndex") questionIndex: Int,
        @ModelAttribute("questionnaire") questionnaire: QuestionnaireWithQuestionDto
    ): String {
        questionnaire.questions.forEach { question ->
            if (question.id == id) {
                for (answer in question.answers) {
                    answerService.updateAnswer(answer)
                }
                return@forEach
            }
        }
        val question = questionService.findQuestionWithAnswers(id)
        returnQuestionsPage(questionnaire.id, model)
        setQuestionWithId(question, questionIndex, model)
        return "fragments/create-questionnaire-answer::question"
    }

    fun returnQuestionsPage(
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
        question: QuestionWithAnswersDto,
        questionIndex: Int,
        model: Model
    ) {
        model.addAttribute("question", question)
        model.addAttribute("questionIndex", questionIndex)
    }
}
