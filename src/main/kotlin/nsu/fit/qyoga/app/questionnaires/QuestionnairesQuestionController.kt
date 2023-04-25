package nsu.fit.qyoga.app.questionnaires

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
        questionService.createQuestion(id)
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
        question.imageId?.let {
            imageService.deleteImage(it)
        }
        val newQuestion = QuestionWithAnswersDto(
            id = question.id,
            title = question.title,
            questionType = question.questionType,
            questionnaireId = question.questionnaireId,
            imageId = imageService.uploadImage(file),
            answers = question.answers
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
                    QuestionWithAnswersDto(
                        id = question.id,
                        title = question.title,
                        questionType = question.questionType,
                        questionnaireId = question.questionnaireId,
                        imageId = question.imageId,
                        answers = question.answers
                    )
                )
            }
        }
        for (answer in questionService.findQuestion(questionId).answers) {
            answerService.deleteAnswerById(answer.id)
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
                    QuestionWithAnswersDto(
                        id = question.id,
                        title = question.title,
                        questionType = question.questionType,
                        questionnaireId = question.questionnaireId,
                        imageId = question.imageId,
                        answers = question.answers
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
