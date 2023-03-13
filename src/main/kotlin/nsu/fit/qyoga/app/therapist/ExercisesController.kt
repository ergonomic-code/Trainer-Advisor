package nsu.fit.qyoga.app.therapist

import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.dtos.CreateExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.users.internal.UserPrincipal
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors
import java.util.stream.IntStream

@Controller
@RequestMapping("/exercises")
class ExercisesController(
    private val exercisesService: ExercisesService
) {
    /**
     * Отображение страницы при пагинации
     */
    @GetMapping
    fun getExercises(
        @ModelAttribute("searchDto") searchDto: ExerciseSearchDto,
        @RequestParam(value = "pageSize", required = false, defaultValue = "10") pageSize: Int,
        @RequestParam(value = "pageNumber", required = false, defaultValue = "1") pageNumber: Int,
        model: Model
    ): String {
        val exercises = exercisesService.getExercises(
            searchDto,
            PageRequest.of(pageNumber - 1, pageSize)
        )
        addExercisePageAttributes(model, exercises, exercisesService)
        return "exercise-search"
    }

    /**
     * Фильтрация упражнений
     */
    @GetMapping("/search")
    fun getExercisesFiltered(
        @ModelAttribute("searchDto") searchDto: ExerciseSearchDto,
        @RequestParam(value = "pageSize", required = false, defaultValue = "10") pageSize: Int,
        @RequestParam(value = "pageNumber", required = false, defaultValue = "1") pageNumber: Int,
        model: Model
    ): String {
        val exercises = exercisesService.getExercises(
            searchDto,
            PageRequest.of(pageNumber - 1, pageSize)
        )
        addExercisePageAttributes(model, exercises, exercisesService)
        return "exercise-search :: exercises"
    }

    /**
     * Получение страницы создания упражнения
     */
    @GetMapping("/create")
    fun getCreateExercisePage(
        model: Model
    ): String {
        return "exercise-create"
    }

    @PostMapping
    fun createExercise(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @ModelAttribute("createExerciseDto") createExerciseDto: CreateExerciseDto,
        model: Model
    ): String {
        println(userPrincipal.getId())
        exercisesService.createExercise(createExerciseDto, userPrincipal.getId())
        val exercises = exercisesService.getExercises(ExerciseSearchDto(), PageRequest.of(0, 10))
        addExercisePageAttributes(model, exercises, exercisesService)
        return "exercise-search"
    }

    fun addExercisePageAttributes(model: Model, exercises: Page<ExerciseDto>, exercisesService: ExercisesService) {
        model.addAttribute("searchDto", ExerciseSearchDto())
        model.addAttribute("types", exercisesService.getExerciseTypes())
        model.addAttribute("exercises", exercises)
        model.addAttribute(
            "pageNumbers",
            IntStream.rangeClosed(1, exercises.totalPages).boxed().collect(Collectors.toList())
        )
    }
}
