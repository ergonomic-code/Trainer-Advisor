package pro.qyoga.app.therapist.therapy.therapeutic_tasks

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTasksService
import pro.qyoga.platform.spring.mvc.modelAndView


@Controller
@RequestMapping("/therapist/therapeutic-tasks/search")
class SearchTherapeuticTasksController(
    private val therapeuticTasksService: TherapeuticTasksService
) {

    @GetMapping
    fun search(
        @RequestParam("therapeuticTaskName") searchKey: String
    ): ModelAndView {
        val tasks = therapeuticTasksService.findByNameContaining(searchKey)
        return modelAndView("therapist/therapy/therapeutic-tasks/search-result") {
            "tasks" bindTo tasks
        }
    }

}