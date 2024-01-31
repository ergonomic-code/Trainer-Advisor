package pro.qyoga.app.therapist.therapy.therapeutic_tasks

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.qyoga.core.therapy.therapeutic_tasks.TherapeuticTasksRepo
import pro.qyoga.core.therapy.therapeutic_tasks.errors.DuplicatedTherapeuticTaskName
import pro.qyoga.core.therapy.therapeutic_tasks.findByNameContaining
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.core.users.internal.QyogaUserDetails
import pro.qyoga.platform.spring.mvc.modelAndView
import pro.qyoga.platform.spring.sdj.erpo.hydration.resolveOrThrow

private const val TASKS = "tasks"

const val DUPLICATED_NEW_TASK_NAME = "duplicatedNewTaskName"

const val DUPLICATED_EDITED_TASK_NAME = "duplicatedEditedTaskName"

@Controller
@RequestMapping("/therapist/therapeutic-tasks")
class TherapeuticTasksListsPageController(
    private val therapeuticTasksRepo: TherapeuticTasksRepo,
    private val deleteTherapeuticTaskWorkflow: DeleteTherapeuticTaskWorkflow
) {

    @GetMapping
    fun getTherapeuticTasksList(
        @AuthenticationPrincipal principal: QyogaUserDetails
    ): ModelAndView {
        return modelAndView("therapist/therapy/therapeutic-tasks/therapeutic-tasks-list") {
            TASKS bindTo therapeuticTasksRepo.findByNameContaining(
                principal.id,
                null,
                TherapeuticTasksRepo.Page.topTenByName
            )
        }
    }

    @GetMapping("/search")
    fun search(
        @RequestParam searchKey: String?,
        @AuthenticationPrincipal principal: QyogaUserDetails
    ): ModelAndView {
        val tasks =
            therapeuticTasksRepo.findByNameContaining(principal.id, searchKey, TherapeuticTasksRepo.Page.topTenByName)
        return modelAndView("therapist/therapy/therapeutic-tasks/therapeutic-tasks-list :: tasks-list-data") {
            TASKS bindTo tasks
        }
    }

    @PostMapping
    fun create(
        @RequestParam taskName: String,
        @AuthenticationPrincipal principal: QyogaUserDetails
    ): ModelAndView {
        val res = runCatching {
            val newTask = TherapeuticTask(principal.id, taskName)
            therapeuticTasksRepo.save(newTask)
        }

        val persistedTask = when (val ex = res.exceptionOrNull()) {
            is DuplicatedTherapeuticTaskName ->
                return modelAndView("therapist/therapy/therapeutic-tasks/therapeutic-tasks-list :: tasks-list") {
                    "task" bindTo ex.failedToSaveTask
                    DUPLICATED_NEW_TASK_NAME bindTo true
                }

            else -> res.getOrThrow()
        }

        return modelAndView("therapist/therapy/therapeutic-tasks/therapeutic-tasks-list :: tasks-list") {
            TASKS bindTo listOf(persistedTask)
        }
    }

    @PutMapping("/{taskId}")
    fun edit(
        @PathVariable taskId: Long,
        @RequestParam taskName: String,
    ): ModelAndView {
        val res = runCatching {
            therapeuticTasksRepo.update(taskId) {
                it.withName(taskName)
            }
        }

        val persistedTask = when (val ex = res.exceptionOrNull()) {
            is DuplicatedTherapeuticTaskName ->
                return modelAndView("therapist/therapy/therapeutic-tasks/therapeutic-tasks-list :: tasks-list") {
                    TASKS bindTo listOf(ex.failedToSaveTask)
                    DUPLICATED_EDITED_TASK_NAME bindTo true
                }

            else -> res.getOrThrow()
        }

        return modelAndView("therapist/therapy/therapeutic-tasks/therapeutic-tasks-list :: tasks-list") {
            TASKS bindTo listOf(persistedTask)
            "updateSuccess" bindTo true
        }
    }

    @DeleteMapping("/{taskId}")
    fun delete(
        @PathVariable taskId: Long,
    ): Any {
        val res = runCatching {
            deleteTherapeuticTaskWorkflow(taskId)
        }

        when (val ex = res.exceptionOrNull()) {
            is TherapeuticTaskHasReferences ->
                return modelAndView("therapist/therapy/therapeutic-tasks/therapeutic-tasks-list :: tasks-list") {
                    TASKS bindTo listOf(ex.taskRef.resolveOrThrow())
                    "taskHasReferencesError" bindTo true
                    "references" bindTo ex.references
                }

            else ->
                res.getOrThrow()
        }

        return ResponseEntity.ok(null)
    }

}