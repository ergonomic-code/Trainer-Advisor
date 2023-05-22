package nsu.fit.qyoga.app.completingQuestionnaires

import jakarta.servlet.http.HttpSession
import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingSearchDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.services.CompletingService
import nsu.fit.qyoga.core.therapists.api.TherapistService
import nsu.fit.qyoga.core.users.api.UserService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal

@Controller
@RequestMapping("/therapist/questionnaires")
class QuestionnaireSingleController(
    private val completingService: CompletingService,
    private val httpSession: HttpSession,
    private val userService: UserService,
    private val therapistService: TherapistService
) {

    /**
     * Просмотр опросника
     */
    @GetMapping("/performing")
    fun getQuestionnaireById(
        @ModelAttribute("completingSearchDto") completingSearchDto: CompletingSearchDto,
        @PageableDefault(value = 10, page = 0, sort = ["date"]) pageable: Pageable,
        principal: Principal,
        model: Model
    ): String {
        val therapistId = httpSession.getAttribute("therapistId") as Long?
            ?: let {
                val userId = userService.findByUsername(principal.name)!!.id
                print("myId $userId")
                val id = therapistService.findTherapistByUserId(userId)!!.id
                httpSession.setAttribute("therapistId", id)
                id
            }
        model.addAttribute(
            "results",
            completingService.findCompletingByTherapistId(
                therapistId,
                completingSearchDto,
                pageable
            )
        )
        return "completingQuestionnaires/questionnaire-page"
    }

    /**
     * Фильтрация прохождений
     */
    @GetMapping("/{id}/action")
    fun sortQuestionnaires(
        @PathVariable id: Long,
        @ModelAttribute("completingSearchDto") completingSearchDto: CompletingSearchDto,
        @PageableDefault(value = 10, page = 0, sort = ["date"]) pageable: Pageable,
        model: Model,
        principal: Principal
    ): String {
        /*val clientId = httpSession.getAttribute("clientId") as Long?
            ?: userService.findUserIdByUsername(principal.name)
        model.addAttribute(
            "results",
            completingService.findCompletingByQId(
                id,
                clientId,
                completingSearchDto,
                pageable
            )
        )*/

        return "questionnaire/questionnaire-list :: page-content"
    }
}
