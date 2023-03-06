package nsu.fit.qyoga.core.questionnaires.ports

import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.questionnaires.api.QuestionnaireService
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireSearchDto
import nsu.fit.qyoga.core.questionnaires.utils.OrderType
import nsu.fit.qyoga.core.questionnaires.utils.Page
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.stream.Collectors
import java.util.stream.IntStream

@Controller
@RequestMapping("/questionnaires/")
class QuestionnairesController(
    private val questionnaireService: QuestionnaireService
){

    @GetMapping()
    fun getQuestionnairesList(model: Model) : String{
        model.addAttribute("questionnaires", questionnaireService.findQuestionnaires(null, Page()))
        return "questionnaire/QuestionnaireList.html"
    }

    @PostMapping("/sort")
    fun sortQuestionnaires(
        @RequestParam("searchDto") searchDto: QuestionnaireSearchDto,
        model: Model
    ): String{
        addQuestionnairePageAttributes(model, searchDto)
        return "QuestionnairePages/QuestionnaireList :: questionnaires"
    }

    @PostMapping("/search")
    fun searchQuestionnaires(
        @RequestParam("searchDto") searchDto: QuestionnaireSearchDto,
        model: Model
    ): String{
        addQuestionnairePageAttributes(model, searchDto)
        return "QuestionnairePages/QuestionnaireList :: questionnaires"
    }

    fun addQuestionnairePageAttributes(model: Model, searchDto: QuestionnaireSearchDto) {
        model.addAttribute("questionnaires", questionnaireService.findQuestionnaires(searchDto.title, Page(orderType = searchDto.page.orderType)))
        model.addAttribute("searchDto",
            QuestionnaireSearchDto(
                title = searchDto.title,
                page = Page(
                    orderType = searchDto.page.orderType,
                    totalElements = questionnaireService.getQuestionnairesCount(searchDto.title))))
    }

}