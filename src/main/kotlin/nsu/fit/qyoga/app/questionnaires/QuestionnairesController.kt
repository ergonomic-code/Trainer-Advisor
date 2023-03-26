package nsu.fit.qyoga.app.questionnaires

import nsu.fit.qyoga.core.questionnaires.api.dtos.*
import nsu.fit.qyoga.core.questionnaires.api.services.AnswerService
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionService
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/therapist/questionnaires")
class QuestionnairesController(
    private val questionnaireService: QuestionnaireService,
    private val questionService: QuestionService,
    private val answerService: AnswerService
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
    @GetMapping("new/{id}/add-question")
    fun addNewQuestionToQuestionnaire(
        @PathVariable id: Long,
        model: Model
    ): String {
        model.addAttribute("question", questionService.createQuestion(id))
        return ""
    }

    @PostMapping("new/{id}")
    fun createQuestionnaire(
        @ModelAttribute("createQuestionnaire") questionnaire: QuestionnaireWithQuestionDto,
        @PathVariable id: Long
    ): String {
        return "redirect:/questionnaires/"
    }

    @GetMapping("new/{id}/image")
    fun loadImageToPage(
        @RequestParam("file") file: MultipartFile,
        @PathVariable id: Long
    ): String {
        return ""
    }

    @PostMapping("new/{id}/image")
    fun addImageToQuestion(
        @RequestParam("file") file: MultipartFile,
        @PathVariable id: Long
    ): String {
        return ""
    }

    @DeleteMapping("new/{id}/image")
    fun deleteImageFromQuestion(
        @PathVariable id: Long
    ): String {
        return ""
    }

    @GetMapping("new/{id}/change-type")
    fun changeAnswerType(
        @RequestParam("question-type") type: String,
        @PathVariable id: Long
    ): String {
        println(type)
        return ""
    }

    @GetMapping("new/{questionNum}/{questionId}/setScores")
    fun setQuestionScore(
        @PathVariable questionId: Int,
        model : Model,
        @PathVariable questionNum: Int
    ): String {
        return ""
    }

    @GetMapping("new/{questionNum}/{questionId}/setAnswers")
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