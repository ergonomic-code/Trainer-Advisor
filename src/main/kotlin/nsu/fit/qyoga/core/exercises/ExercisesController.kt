package nsu.fit.qyoga.core.exercises

import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.stream.Collectors
import java.util.stream.IntStream

@Controller
@RequestMapping("/exercises/")
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
            searchDto.title,
            searchDto.contradiction,
            searchDto.duration,
            searchDto.exerciseType,
            searchDto.therapeuticPurpose,
            PageRequest.of(pageNumber - 1, pageSize)
        )
        addExercisePageAttributes(model, exercises, exercisesService)
        return "exercise-search"
    }

    /**
     * Получение только списка упражнений в качестве элемента отображения
     */
    @GetMapping("search")
    fun searchExercises(
        @ModelAttribute("searchDto") searchDto: ExerciseSearchDto,
        @RequestParam(value = "pageSize", required = false, defaultValue = "10") pageSize: Int,
        @RequestParam(value = "pageNumber", required = false, defaultValue = "1") pageNumber: Int,
        model: Model
    ): String {
        model.addAttribute("searchDto", searchDto)
        val exercises = exercisesService.getExercises(
            searchDto.title,
            searchDto.contradiction,
            searchDto.duration,
            searchDto.exerciseType,
            searchDto.therapeuticPurpose,
            PageRequest.of(pageNumber - 1, pageSize)
        )
        model.addAttribute("exercises", exercises)
        return "exercise-search :: exercises"
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
