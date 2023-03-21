package nsu.fit.qyoga.app.questionnaires

import nsu.fit.qyoga.core.questionnaires.api.dtos.CreateQuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireSearchDto
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
    private val questionnaireService: QuestionnaireService
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

    @GetMapping("new")
    fun getCreateQuestionnairePage(model: Model): String {
        return "questionnaire/createQuestionnaire"
    }

    @PostMapping("new")
    fun createQuestionnaire(
        @ModelAttribute("createQuestionnaire") createQuestionnaireDto: CreateQuestionnaireDto
    ): String {
        return "redirect:/questionnaires/"
    }

    @PostMapping("new/question/{id}/addImage")
    fun addImageToQuestion(
        @RequestParam("file") file: MultipartFile,
        @PathVariable id: String
    ): String {
        return ""
    }

    @PostMapping("new/answer/{id}/addImage")
    fun addImageToAnswer(
        @RequestParam("file") file: MultipartFile,
        @PathVariable id: String
    ): String {
        return ""
    }

    @GetMapping("new/{questionId}/changeType")
    fun changeAnswerType(
        @RequestParam("question-type") type: String,
        @PathVariable questionId: String
    ): String {
        println(type)
        return ""
    }

    @GetMapping("new/{questionnaireId}/addQuestion")
    fun addNewQuestionToQuestionnaire(
        @PathVariable questionnaireId: String
    ): String {
        println("New Question!")
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
