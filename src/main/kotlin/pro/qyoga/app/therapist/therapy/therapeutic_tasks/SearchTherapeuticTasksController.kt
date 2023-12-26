package pro.qyoga.app.therapist.therapy.therapeutic_tasks

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.core.therapy.therapeutic_tasks.internal.TherapeuticTasksRepo
import pro.qyoga.platform.spring.mvc.modelAndView
import pro.qyoga.platform.spring.sdj.withSortBy

private val therapeuticTaskAutocompletionPage = Pageable.ofSize(5).withSortBy(TherapeuticTask::name)

@Controller
@RequestMapping("/therapist/therapeutic-tasks/autocomplete-search")
class SearchTherapeuticTasksController(
    private val therapeuticTasksRepo: TherapeuticTasksRepo
) {

    @GetMapping
    fun search(
        @RequestParam("therapeuticTaskName") searchKey: String
    ): ModelAndView {
        val tasks = therapeuticTasksRepo.findByNameContaining(searchKey, therapeuticTaskAutocompletionPage)
        return modelAndView("therapist/therapy/therapeutic-tasks/therapeutic-tasks-search-result") {
            "tasks" bindTo tasks
        }
    }

}