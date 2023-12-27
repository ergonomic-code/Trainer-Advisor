package pro.qyoga.app.therapist.therapy.therapeutic_tasks

import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import pro.qyoga.core.therapy.therapeutic_tasks.api.DuplicatedTherapeuticTaskName
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.core.therapy.therapeutic_tasks.internal.TherapeuticTasksRepo
import pro.qyoga.core.users.internal.QyogaUserDetails
import pro.qyoga.platform.kotlin.isFailureOf
import pro.qyoga.platform.spring.mvc.modelAndView
import pro.qyoga.platform.spring.sdj.withSortBy

private val therapeuticTaskListPage = Pageable.ofSize(10)
    .withSortBy(TherapeuticTask::name)

private const val TASKS = "tasks"

const val DUPLICATED_TASK_NAME = "duplicatedTaskName"

@Controller
@RequestMapping("/therapist/therapeutic-tasks")
class TherapeuticTasksListsPageController(
    private val therapeuticTasksRepo: TherapeuticTasksRepo
) {

    @GetMapping
    fun getTherapeuticTasksList(): ModelAndView {
        return modelAndView("therapist/therapy/therapeutic-tasks/therapeutic-tasks-list") {
            TASKS bindTo therapeuticTasksRepo.findByNameContaining(null, therapeuticTaskListPage)
        }
    }

    @GetMapping("/search")
    fun search(
        @RequestParam searchKey: String?
    ): ModelAndView {
        val tasks = therapeuticTasksRepo.findByNameContaining(searchKey, therapeuticTaskListPage)
        return modelAndView("therapist/therapy/therapeutic-tasks/therapeutic-tasks-list :: tasks-list-data") {
            TASKS bindTo tasks
        }
    }

    @PostMapping
    fun create(
        @RequestParam taskName: String,
        @AuthenticationPrincipal principal: QyogaUserDetails
    ): ModelAndView {
        val task = TherapeuticTask(AggregateReference.to(principal.id), taskName)
        val res = runCatching {
            therapeuticTasksRepo.save(task)
        }

        if (res.isFailureOf<DuplicatedTherapeuticTaskName>()) {
            return modelAndView("therapist/therapy/therapeutic-tasks/therapeutic-tasks-list :: tasks-list") {
                "task" bindTo task
                DUPLICATED_TASK_NAME bindTo true
            }
        }

        val persistedTask = res.getOrThrow()

        return modelAndView("therapist/therapy/therapeutic-tasks/therapeutic-tasks-list :: tasks-list") {
            TASKS bindTo listOf(persistedTask)
        }
    }

}