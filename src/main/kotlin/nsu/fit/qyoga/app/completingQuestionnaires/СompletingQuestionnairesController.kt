package nsu.fit.qyoga.app.completingQuestionnaires

import jakarta.servlet.http.HttpSession
import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingSearchDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.services.CompletingService
import nsu.fit.qyoga.core.users.api.UserService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal

@Controller
@RequestMapping("/therapist/questionnaires/performing")
class СompletingQuestionnairesController(
    private val completingService: CompletingService,
    private val httpSession: HttpSession,
    private val userService: UserService
) {

    /**
     * Просмотр опросника
     */
    @GetMapping("")
    fun getQuestionnaireById(
        @ModelAttribute("completingSearchDto") completingSearchDto: CompletingSearchDto,
        @PageableDefault(value = 10, page = 0, sort = ["date"]) pageable: Pageable,
        principal: Principal,
        model: Model
    ): String {
        val therapistId = httpSession.getAttribute("therapistId") as Long?
            ?: let {
                val userId = userService.findByUsername(principal.name)!!.id
                httpSession.setAttribute("therapistId", userId)
                userId
            }
        val completingList = completingService.findCompletingByTherapistId(
            therapistId,
            completingSearchDto,
            pageable
        )
        model.addAttribute(
            "results",
            completingList
        )
        model.addAttribute(
            "completingSearchDto",
            completingSearchDto
        )
        model.addAttribute(
            "sortType",
            completingList.sort.getOrderFor("date").toString().substringAfter(' ')
        )
        return "completingQuestionnaires/questionnaire-page"
    }

    /**
     * Фильтрация прохождений
     */
    @GetMapping("/action")
    fun sortQuestionnaires(
        @ModelAttribute("completingSearchDto") completingSearchDto: CompletingSearchDto,
        @PageableDefault(value = 10, page = 0, sort = ["date"]) pageable: Pageable,
        model: Model,
        principal: Principal
    ): String {
        print("HI")
        val therapistId = httpSession.getAttribute("therapistId") as Long?
            ?: let {
                val userId = userService.findByUsername(principal.name)!!.id
                httpSession.setAttribute("therapistId", userId)
                userId
            }
        val completingList = completingService.findCompletingByTherapistId(
            therapistId,
            completingSearchDto,
            pageable
        )
        model.addAttribute(
            "results",
            completingList
        )
        model.addAttribute(
            "completingSearchDto",
            completingSearchDto
        )
        model.addAttribute(
            "sortType",
            completingList.sort.getOrderFor("date").toString().substringAfter(' ')
        )
        return "completingQuestionnaires/questionnaire-page :: page-content"
    }
}
