package nsu.fit.qyoga.app.therapist

import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.exercises.api.dtos.ModifiableExerciseDto
import nsu.fit.qyoga.core.users.internal.UserDetailsImpl
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors
import java.util.stream.IntStream

private const val BASIC_PAGE_SIZE = 10

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
        return "exercises/exercise-search"
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
        return "exercises/exercise-search :: exercises"
    }

    /**
     * Получение страницы создания упражнения
     */
    @GetMapping("/create")
    fun getCreateExercisePage(
        model: Model
    ): String {
        model.addAttribute("createDto", ModifiableExerciseDto())
        model.addAttribute("types", exercisesService.getExerciseTypes())
        return "exercises/exercise-create"
    }

    /**
     * Создание и упражнения и переход на страницу поиска упражнений
     */
    @PostMapping
    fun createExercise(
        @ModelAttribute("createDto") createDto: ModifiableExerciseDto,
        authentication: Authentication,
        model: Model
    ): String {
        val userPrincipal = authentication.principal as UserDetailsImpl
        exercisesService.createExercise(createDto, userPrincipal.getId())
        val exercises = exercisesService.getExercises(ExerciseSearchDto(), PageRequest.of(0, BASIC_PAGE_SIZE))
        addExercisePageAttributes(model, exercises, exercisesService)
        return "exercises/exercise-search"
    }

    @PutMapping
    fun editExercise(
        @ModelAttribute("exercise") editDto: ModifiableExerciseDto,
        model: Model
    ): String {
        exercisesService.editExercise(editDto)
        val exercises = exercisesService.getExercises(ExerciseSearchDto(), PageRequest.of(0, BASIC_PAGE_SIZE))
        addExercisePageAttributes(model, exercises, exercisesService)
        return "exercises/exercise-search"
    }

    /**
     * Получение упражнения по id и переход на страницу редактирования упражнения
     */
    @GetMapping("/{id}")
    fun getExerciseById(
        @PathVariable id: Long,
        model: Model
    ): String {
        val exercise = exercisesService.getExerciseById(id)
        model.addAttribute("exercise", exercise)
        model.addAttribute("types", exercisesService.getExerciseTypes())
        return "exercises/exercise-edit"
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
