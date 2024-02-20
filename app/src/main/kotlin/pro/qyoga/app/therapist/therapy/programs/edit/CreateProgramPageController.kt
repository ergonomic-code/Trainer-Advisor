package pro.qyoga.app.therapist.therapy.programs.edit

import org.springframework.data.domain.PageRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.http.hxRedirect
import pro.qyoga.app.platform.EntityPageMode
import pro.qyoga.core.therapy.exercises.ExercisesService
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.programs.dtos.CreateProgramRequest
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails


@Controller
@RequestMapping("/therapist/programs/create")
class CreateProgramPageController(
    private val createProgram: CreateProgram,
    private val exercisesService: ExercisesService
) {

    @GetMapping
    fun getCreateProgramPage(): ModelAndView {
        return programPageModelAndView(EntityPageMode.CREATE)
    }

    @PostMapping
    fun createProgram(
        createProgramRequest: CreateProgramRequest,
        therapeuticTaskName: String,
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): Any = when (createProgram(createProgramRequest, therapeuticTaskName, therapist.id)) {
        is CreateProgramResult.InvalidTherapeuticTaskName ->
            programPageModelAndView(
                pageMode = EntityPageMode.CREATE,
                program = ProgramPageModel(createProgramRequest, therapeuticTaskName),
                fragment = "programForm"
            ) {
                NOT_EXISTING_THERAPEUTIC_TASK bindTo true
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