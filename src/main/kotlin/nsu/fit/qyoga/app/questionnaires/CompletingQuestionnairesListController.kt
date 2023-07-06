package nsu.fit.qyoga.app.questionnaires

import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingSearchDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.services.CompletingService
import nsu.fit.qyoga.core.users.internal.QyogaUserDetails
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/therapist/questionnaires/performing")
class CompletingQuestionnairesListController(
    private val completingService: CompletingService
) {

    /**
     * Просмотр опросника
     */
    @GetMapping("")
    fun getQuestionnaireById(
        @ModelAttribute("completingSearchDto") completingSearchDto: CompletingSearchDto,
        @PageableDefault(value = 10, page = 0, sort = ["date"]) pageable: Pageable,
        @AuthenticationPrincipal principal: QyogaUserDetails,
        model: Model
    ): String {
        val completingList = completingService.findCompletingByTherapistId(
            principal.id,
            completingSearchDto,
            pageable
        )
        addPageAttributes(model, completingList, completingSearchDto)
        return "completingQuestionnaires/questionnaire-page"
    }

    /**
     * Фильтрация прохождений
     */
    @GetMapping("", headers = ["action=true"])
    fun sortQuestionnaires(
        @ModelAttribute("completingSearchDto") completingSearchDto: CompletingSearchDto,
        @PageableDefault(value = 10, page = 0, sort = ["date"]) pageable: Pageable,
        @AuthenticationPrincipal principal: QyogaUserDetails,
        model: Model
    ): String {
        val completingList = completingService.findCompletingByTherapistId(
            principal.id,
            completingSearchDto,
            pageable
        )
        addPageAttributes(model, completingList, completingSearchDto)
        return "completingQuestionnaires/questionnaire-page :: page-content"
    }

    fun addPageAttributes(
        model: Model,
        completingList: Page<CompletingDto>,
        completingSearchDto: CompletingSearchDto
    ) {
        model.addAllAttributes(
            mapOf(
                "results" to completingList,
                "completingSearchDto" to completingSearchDto,
                "sortType" to completingList.sort.getOrderFor("date").toString().substringAfter(' ')
            )
        )
    }
}
