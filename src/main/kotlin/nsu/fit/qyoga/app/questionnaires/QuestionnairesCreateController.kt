package nsu.fit.qyoga.app.questionnaires

import jakarta.servlet.http.HttpSession
import nsu.fit.qyoga.core.questionnaires.api.dtos.*
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/therapist/questionnaires")
class QuestionnairesCreateController(
    private val questionnaireService: QuestionnaireService,
    private val httpSession: HttpSession
) {

    /**
     * Получение страницы редактирования
     */
    @GetMapping("/edit")
    fun getCreateQuestionnairePage(): String {
        val questionnaire = getQuestionnaireFromSession()
        if (questionnaire == null) {
            httpSession.setAttribute(
                "questionnaire",
                CreateQuestionnaireDto(
                    id = 0,
                    title = "",
                    question = mutableListOf(CreateQuestionDto(answers = listOf(CreateAnswerDto()))),
                    decoding = mutableListOf(DecodingDto())
                )
            )
        }
        return "questionnaire/create-questionnaire"
    }

    /**
     * Создание нового опросника
     */
    @GetMapping("/new")
    fun createQuestionnaire(): String {
        httpSession.setAttribute(
            "questionnaire",
            CreateQuestionnaireDto(
                id = 0,
                title = "",
                question = mutableListOf(CreateQuestionDto(answers = listOf(CreateAnswerDto()))),
                decoding = mutableListOf(DecodingDto())
            )
        )
        return "redirect:/therapist/questionnaires/edit"
    }

    /**
     * Редактирование опросника
     */
    @GetMapping("/{id}/edit")
    fun editQuestionnaire(
        @PathVariable id: Long
    ): String {
        httpSession.setAttribute(
            "questionnaire",
            questionnaireService.findQuestionnaireWithQuestions(id)
        )
        return "redirect:/therapist/questionnaires/edit"
    }

    /**
     * Создание опросника
     */
    @PostMapping("/edit")
    fun createQuestionnaire(
        @ModelAttribute("questionnaire") questionnaire: CreateQuestionnaireDto,
    ): String {
        httpSession.setAttribute("questionnaire", questionnaire)
        return "redirect:/therapist/questionnaires/edit/setResult"
    }


    /**
     * Задание заголовка опросника
     */
    @PostMapping("/edit/title")
    @ResponseBody
    fun changeQuestionnaireTitle(
        @RequestParam title: String
    ): HttpStatus {
        val questionnaire = getQuestionnaireFromSession()
            ?: return HttpStatus.NOT_FOUND
        httpSession.setAttribute("questionnaire", questionnaire.copy(title = title))
        return HttpStatus.OK
    }

    fun getQuestionnaireFromSession(): CreateQuestionnaireDto? {
        return httpSession.getAttribute("questionnaire") as CreateQuestionnaireDto?
    }
}
