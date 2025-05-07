package pro.qyoga.app.therapist.therapy.therapeutic_tasks

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.qyoga.core.therapy.therapeutic_tasks.TherapeuticTasksRepo
import pro.qyoga.core.therapy.therapeutic_tasks.errors.DuplicatedTherapeuticTaskName
import pro.qyoga.core.therapy.therapeutic_tasks.findTherapistTasksSliceByName
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails

private const val TASKS = "tasks"

const val DUPLICATED_NEW_TASK_NAME = "duplicatedNewTaskName"

const val DUPLICATED_EDITED_TASK_NAME = "duplicatedEditedTaskName"

@Controller
@RequestMapping("/therapist/therapeutic-tasks")
class TherapeuticTasksListsPageController(
    private val therapeuticTasksRepo: TherapeuticTasksRepo,
    private val deleteTherapeuticTask: DeleteTherapeuticTaskOp
) {

    @GetMapping
    fun getTherapeuticTasksList(
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): ModelAndView {
        return modelAndView(
            "therapist/therapy/therapeutic-tasks/therapeutic-tasks-list", mapOf(
                TASKS to therapeuticTasksRepo.findTherapistTasksSliceByName(
                    therapist.id,
                    null,
                    TherapeuticTasksRepo.Page.topTenByName
                )
            )
        )
    }

    @GetMapping("/search")
    fun search(
        @RequestParam searchKey: String?,
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): ModelAndView {
        val tasks =
            therapeuticTasksRepo.findTherapistTasksSliceByName(
                therapist.id,
                searchKey,
                TherapeuticTasksRepo.Page.topTenByName
            )
        return modelAndView(
            "therapist/therapy/therapeutic-tasks/therapeutic-tasks-list :: tasks-list-data", mapOf(
                TASKS to tasks,
            )
        )
    }

    @PostMapping
    fun create(
        @RequestParam taskName: String,
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): ModelAndView {
        val res = runCatching {
            val newTask = TherapeuticTask(therapist.id, taskName)
            therapeuticTasksRepo.save(newTask)
        }

        val persistedTask = when (val ex = res.exceptionOrNull()) {
            is DuplicatedTherapeuticTaskName ->
                return modelAndView(
                    "therapist/therapy/therapeutic-tasks/therapeutic-tasks-list :: tasks-list", mapOf(
                        "task" to ex.failedToSaveTask,
                        DUPLICATED_NEW_TASK_NAME to true,
                    )
                )

            else ->
                res.getOrThrow()
        }

        return modelAndView(
            "therapist/therapy/therapeutic-tasks/therapeutic-tasks-list :: tasks-list", mapOf(
                TASKS to listOf(persistedTask),
            )
        )
    }

    @PutMapping("/{taskId}")
    fun edit(
        @PathVariable taskId: Long,
        @RequestParam taskName: String,
    ): ModelAndView {
        val res = runCatching {
            therapeuticTasksRepo.updateById(taskId) {
                it.withName(taskName)
            }
        }

        val persistedTask = when (val ex = res.exceptionOrNull()) {
            is DuplicatedTherapeuticTaskName ->
                return modelAndView(
                    "therapist/therapy/therapeutic-tasks/therapeutic-tasks-list :: tasks-list", mapOf(
                        TASKS to listOf(ex.failedToSaveTask),
                        DUPLICATED_EDITED_TASK_NAME to true,
                    )
                )

            else ->
                res.getOrThrow()
        }

        return modelAndView(
            "therapist/therapy/therapeutic-tasks/therapeutic-tasks-list :: tasks-list", mapOf(
                TASKS to listOf(persistedTask),
                "updateSuccess" to true,
            )
        )
    }

    @DeleteMapping("/{taskId}")
    fun delete(
        @PathVariable taskId: Long,
    ): Any {
        val res = runCatching {
            deleteTherapeuticTask(taskId)
        }

        when (val ex = res.exceptionOrNull()) {
            is TherapeuticTaskHasReferences ->
                return modelAndView(
                    "therapist/therapy/therapeutic-tasks/therapeutic-tasks-list :: tasks-list", mapOf(
                        TASKS to listOf(ex.taskRef.resolveOrThrow()),
                        "taskHasReferencesError" to true,
                        "references" to ex.references,
                    )
                )

            else ->
                res.getOrThrow()
        }

        return ResponseEntity.ok(null)
    }

}