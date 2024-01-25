package pro.qyoga.app.therapist.therapy.programs

import org.springframework.data.domain.PageRequest
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.qyoga.core.therapy.exercises.ExercisesService
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.core.users.internal.QyogaUserDetails
import pro.qyoga.platform.spring.http.hxRedirect
import pro.qyoga.platform.spring.mvc.modelAndView
import pro.qyoga.platform.spring.sdj.erpo.hydration.AggregateReferenceTarget


@Controller
@RequestMapping("/therapist/programs/create")
class CreateProgramPageController(
    private val createProgram: CreateProgram,
    private val exercisesService: ExercisesService
) {

    @GetMapping
    fun getCreateProgramPage(): ModelAndView {
        return modelAndView("/therapist/therapy/programs/program-edit.html") {}
    }

    @PostMapping
    fun createProgram(
        createProgramRequest: CreateProgramRequest,
        therapeuticTaskName: String,
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): Any = when (createProgram(createProgramRequest, therapeuticTaskName, therapist.id)) {
        is CreateProgramResult.InvalidTherapeuticTaskName ->
            modelAndView("/therapist/therapy/programs/program-edit.html :: editForm") {
                "program" bindTo Program.of(
                    createProgramRequest,
                    AggregateReferenceTarget(TherapeuticTask(therapist.id, therapeuticTaskName)),
                    AggregateReference.to(therapist.id)
                )
            }

        is CreateProgramResult.Success ->
            hxRedirect("/therapist/programs")
    }

    @GetMapping("/search-exercises")
    @ResponseBody
    fun searchExercises(@RequestParam searchKey: String?): Iterable<ExerciseSummaryDto> {
        return exercisesService.findExerciseSummaries(ExerciseSearchDto(title = searchKey), PageRequest.of(0, 5))
    }

}