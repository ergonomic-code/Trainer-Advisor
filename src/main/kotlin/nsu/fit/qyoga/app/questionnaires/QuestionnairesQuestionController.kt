package nsu.fit.qyoga.app.questionnaires

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionWithAnswersDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireWithQuestionDto
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionException
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
class QuestionnairesQuestionController(
    private val questionnaireService: QuestionnaireService,
    private val questionService: QuestionService,
    private val answerService: AnswerService,
    private val imageService: ImageService
) {

    /**
     * Добавление нового вопроса
     */
    @GetMapping("{id}/edit/add-question")
    fun addNewQuestionToQuestionnaire(
        @PathVariable id: Long,
        model: Model
    ): String {
        val question = questionService.createQuestion(id)
        question.answers += answerService.createAnswer(question.id)
        return returnQuestionsPage(id, model)
    }

    /**
     * Добавление изображение вопросу
     */
    @PostMapping("question/{id}/image")
    fun addImageToQuestion(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("questionIndex") questionIndex: Int,
        @PathVariable id: Long,
        model: Model
    ): String {
        val question = questionService.findQuestion(id)
        if (question.imageId != null) {
            imageService.deleteImage(question.imageId)
        }
        val newQuestion = QuestionDto(
            id = question.id,
            title = question.title,
            questionType = question.questionType,
            questionnaireId = question.questionnaireId,
            imageId = imageService.uploadImage(file)
        )
        val questionDto = questionService.findQuestion(questionService.updateQuestion(newQuestion))
        setQuestionWithId(
            QuestionWithAnswersDto(
                questionDto.id,
                questionDto.title,
                questionDto.questionType,
                questionDto.imageId,
                questionDto.questionnaireId
            ),
            questionIndex,
            model
        )
        return "fragments/create-questionnaire-image::questionImage"
    }

    /**
     * Удаление вопроса из опросника
     */
    @DeleteMapping("{questionnaireId}/edit/question/{questionId}")
    fun deleteQuestionFromQuestionnaire(
        @PathVariable questionId: Long,
        @PathVariable questionnaireId: Long,
        model: Model
    ): String {
        questionService.deleteQuestion(questionId)
        return returnQuestionsPage(questionnaireId, model)
    }

    /**
     * Изменить тип вопроса
     */
    @PostMapping("{questionnaireId}/edit/question/{questionId}/change-type")
    fun changeAnswersType(
        @ModelAttribute("questionnaire") questionnaire: QuestionnaireWithQuestionDto,
        model: Model,
        @PathVariable questionId: Long,
        @PathVariable questionnaireId: Long
    ): String {
        questionnaire.questions.forEach { question ->
            if (question.id == questionId) {
                questionService.updateQuestion(
                    QuestionDto(
                        id = question.id,
                        title = question.title,
                        questionType = question.questionType,
                        questionnaireId = question.questionnaireId,
                        imageId = question.imageId
                    )
                )
            }
        }
        val deletedAnswers = answerService.deleteAllByQuestionId(questionId)
        for (answer in deletedAnswers) {
            answer.imageId?.let {
                imageService.deleteImage(it)
            }
        }
        answerService.createAnswer(questionId)
        return returnQuestionsPage(questionnaireId, model)
    }

    /**
     * Обновить вопрос
     */
    @PostMapping("{questionnaireId}/edit/question/{questionId}/update")
    @ResponseBody
    fun changeQuestionTitle(
        @ModelAttribute("questionnaire") questionnaire: QuestionnaireWithQuestionDto,
        @PathVariable questionId: Long,
        @PathVariable questionnaireId: Long
    ): HttpStatus {
        questionnaire.questions.forEach { question ->
            if (question.id == questionId) {
                questionService.updateQuestion(
                    QuestionDto(
                        id = question.id,
                        title = question.title,
                        questionType = question.questionType,
                        questionnaireId = question.questionnaireId,
                        imageId = question.imageId
                    )
                )
                return HttpStatus.OK
            }
        }
        throw QuestionException("Выбранный вопрос не найден")
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
