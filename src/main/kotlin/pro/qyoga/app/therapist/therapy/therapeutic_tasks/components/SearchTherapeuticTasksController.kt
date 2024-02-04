package pro.qyoga.app.therapist.therapy.therapeutic_tasks.components

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.core.therapy.therapeutic_tasks.TherapeuticTasksRepo
import pro.qyoga.core.therapy.therapeutic_tasks.findByNameContaining
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails

@Controller
@RequestMapping("/therapist/therapeutic-tasks/autocomplete-search")
class SearchTherapeuticTasksController(
    private val therapeuticTasksRepo: TherapeuticTasksRepo
) {

    @GetMapping
    fun search(
        @RequestParam("therapeuticTaskName") searchKey: String,
        @AuthenticationPrincipal therapist: QyogaUserDetails,
    ): ModelAndView {
        val tasks =
            therapeuticTasksRepo.findByNameContaining(therapist.id, searchKey, TherapeuticTasksRepo.Page.topFiveByName)
        return modelAndView("therapist/therapy/therapeutic-tasks/therapeutic-tasks-search-result") {
            "tasks" bindTo tasks
        }
    }

}