package pro.qyoga.app.therapist.therapy.programs.edit

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
import pro.qyoga.platform.spring.sdj.erpo.hydration.AggregateReferenceTarget


@Controller
@RequestMapping("/therapist/programs/create")
class CreateProgramPageController(
    private val createProgram: CreateProgram,
    private val exercisesService: ExercisesService
) {

    @GetMapping
    fun getCreateProgramPage(): ModelAndView {
        return programPageModel(ProgramPageMode.CREATE)
    }

    @PostMapping
    fun createProgram(
        createProgramRequest: CreateProgramRequest,
        therapeuticTaskName: String,
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): Any = when (createProgram(createProgramRequest, therapeuticTaskName, therapist.id)) {
        is CreateProgramResult.InvalidTherapeuticTaskName ->
            programPageModel(
                pageMode = ProgramPageMode.CREATE,
                program = fakeProgramOf(createProgramRequest, therapist, therapeuticTaskName),
                fragment = "programForm"
            ) {
                "notExistingTherapeuticTask" bindTo true
            }

        is CreateProgramResult.Success ->
            hxRedirect("/therapist/programs")
    }

    private fun fakeProgramOf(
        createProgramRequest: CreateProgramRequest,
        therapist: QyogaUserDetails,
        therapeuticTaskName: String
    ) = Program.of(
        createProgramRequest,
        AggregateReferenceTarget(TherapeuticTask(therapist.id, therapeuticTaskName)),
        AggregateReference.to(therapist.id)
    )

    @GetMapping("/search-exercises")
    @ResponseBody
    fun searchExercises(@RequestParam searchKey: String?): Iterable<ExerciseSummaryDto> {
        return exercisesService.findExerciseSummaries(ExerciseSearchDto(title = searchKey), PageRequest.of(0, 5))
    }

}