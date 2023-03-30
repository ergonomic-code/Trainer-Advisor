package nsu.fit.qyoga.app.questionnaires

import nsu.fit.qyoga.core.questionnaires.api.dtos.*
import nsu.fit.qyoga.core.questionnaires.api.services.AnswerService
import nsu.fit.qyoga.core.questionnaires.api.services.ImageService
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionService
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import org.springframework.core.io.InputStreamResource
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/questionnaires/")
class QuestionnairesController(
    private val questionnaireService: QuestionnaireService,
    private val questionService: QuestionService,
    private val answerService: AnswerService,
    private val imageService: ImageService
) {

    @GetMapping()
    fun getQuestionnairesList(
        @ModelAttribute("questionnaireSearchDto") questionnaireSearchDto: QuestionnaireSearchDto,
        @PageableDefault(value = 10, page = 0, sort = ["title"]) pageable: Pageable,
        model: Model
    ): String {
        val questionnaires = questionnaireService.findQuestionnaires(
            questionnaireSearchDto,
            pageable
        )
        addQuestionnairePageAttributes(model, questionnaireSearchDto, questionnaires)
        return "questionnaire/questionnaire-list"
    }

    /**
     * Фильтрация опросников
     */
    @GetMapping("action")
    fun sortQuestionnaires(
        @ModelAttribute("questionnaireSearchDto") questionnaireSearchDto: QuestionnaireSearchDto,
        @PageableDefault(value = 10, page = 0, sort = ["title"]) pageable: Pageable,
        model: Model
    ): String {
        val questionnaires = questionnaireService.findQuestionnaires(
            questionnaireSearchDto,
            pageable
        )
        addQuestionnairePageAttributes(model, questionnaireSearchDto, questionnaires)
        return "questionnaire/questionnaire-list :: page-content"
    }

    /**
     * Создание нового опросника
     */
    @GetMapping("new")
    fun getCreateQuestionnairePage(model: Model): String {
        return "redirect:/questionnaires/${questionnaireService.createQuestionnaire()}/edit"
    }

    /**
     * Редактирование опросника
     */
    @GetMapping("{id}/edit")
    fun editQuestionnaire(
        model: Model,
        @PathVariable id: Long
    ): String {
        val questionnaire = questionnaireService.findQuestionnaireWithQuestions(id)
        val question = questionService.createQuestion(questionnaire.id)
        question.answers.add(answerService.createAnswer(question.id))
        questionnaire.questions.add(question)
        model.addAttribute("questionnaire", questionnaire)
        return "questionnaire/create-questionnaire"
    }

    /**
     * Добавление нового вопроса
     */
    @GetMapping("{id}/edit/add-question")
    fun addNewQuestionToQuestionnaire(
        @PathVariable id: Long,
        model: Model
    ): String {
        val question = questionService.createQuestion(id)
        question.answers.add(answerService.createAnswer(question.id))
        model.addAttribute("question", question)
        return "questionnaire/create-questionnaire::question"
    }

    /**
     * Создание опросника
     */
    @PostMapping("{id}/edit")
    fun createQuestionnaire(
        @ModelAttribute("createQuestionnaire") questionnaire: QuestionnaireWithQuestionDto,
        @PathVariable id: Long
    ): String {
        return "redirect:/questionnaires/"
    }

    @GetMapping("image/{id}")
    @ResponseBody
    fun loadImageToPage(
        @PathVariable id: Long
    ): ResponseEntity<InputStreamResource> {
        val image = imageService.getImage(id)
        return ResponseEntity.ok()
            .contentLength(image.size)
            .contentType(MediaType.parseMediaType(image.mediaType))
            .body(InputStreamResource(image.data.inputStream()))
    }

    @PostMapping("question/{id}/image")
    fun addImageToQuestion(
        @RequestParam("file") file: MultipartFile,
        @PathVariable id: Long,
        model: Model
    ): String {
        val question = questionService.findQuestion(id)
        if( question.imageId != null) {
            imageService.deleteImage(id)
        }
        val questionDto = QuestionDto(
            id = question.id,
            title = question.title,
            questionType = question.questionType,
            questionnaireId = question.questionnaireId,
            imageId = imageService.uploadImage(file)
        )
        questionService.updateQuestion(questionDto)
        model.addAttribute("question", questionDto)
        return "fragments/create-questionnaire-image::questionImage"
    }

    @PostMapping("answer/{id}/image")
    fun addImageToAnswer(
        @RequestParam("file") file: MultipartFile,
        @PathVariable id: Long,
        model: Model
    ): String {
        val answer = answerService.findAnswer(id)
        if( answer.imageId != null) {
            imageService.deleteImage(id)
        }
        val answerDto = AnswerDto(
            id = answer.id,
            title = answer.title,
            lowerBound = answer.lowerBound,
            lowerBoundText = answer.lowerBoundText,
            upperBound = answer.upperBound,
            upperBoundText = answer.upperBoundText,
            score = answer.score,
            imageId = imageService.uploadImage(file),
            questionId = answer.questionId
        )
        answerService.updateAnswer(answerDto)
        model.addAttribute("answer", answerDto)
        return "fragments/create-questionnaire-image::answerImage"
    }

    @DeleteMapping("image/{id}")
    @ResponseBody
    fun deleteImageFromQuestion(
        @PathVariable id: Long
    ): String {
        imageService.deleteImage(id)
        return ""
    }

    /**
     * Удаление вопроса из опросника
     */
    @DeleteMapping("edit/{questionId}")
    fun deleteQuestionFromQuestionnaire(
        @PathVariable questionId: Long,
        model: Model
    ): String {
        model.addAttribute(
            "questionnaire",
            questionnaireService.findQuestionnaireWithQuestions(questionService.deleteQuestion(questionId))
        )
        return "questionnaire/create-questionnaire::questions"
    }

    @GetMapping("{id}/edit/change-type")
    fun changeAnswerType(
        @RequestParam("question-type") type: String,
        @PathVariable id: Long
    ): String {
        println(type)
        return ""
    }

    @GetMapping("edit/{questionNum}/{questionId}/setScores")
    fun setQuestionScore(
        @PathVariable questionId: Int,
        model : Model,
        @PathVariable questionNum: Int
    ): String {
        return ""
    }

    @GetMapping("edit/{questionNum}/{questionId}/setAnswers")
    fun setQuestionAnswers(
        @PathVariable questionId: Int,
        model : Model,
        @PathVariable questionNum: Int
    ): String {
        return ""
    }

    fun addQuestionnairePageAttributes(
        model: Model,
        questionnaireSearchDto: QuestionnaireSearchDto,
        questionnaires: Page<QuestionnaireDto>
    ) {
        model.addAttribute("questionnaires", questionnaires)
        model.addAttribute("questionnaireSearchDto", questionnaireSearchDto)
        model.addAttribute(
            "sortType",
            questionnaires.sort.getOrderFor("title").toString().substringAfter(' ')
        )
    }

}
// при создании опросника нас должно редиректить на страницу его редактирования те с /new/ -> /{id}/edit