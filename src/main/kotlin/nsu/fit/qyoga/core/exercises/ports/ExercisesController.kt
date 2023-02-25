package nsu.fit.qyoga.core.exercises.ports

import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import nsu.fit.qyoga.core.exercises.utils.view.addExercisePageAttributes
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/exercises/")
class ExercisesController(
    private val exercisesService: ExercisesService
) {

    /**
     * Отображение страницы со списком упражнений при первом переходе в раздел упражнеий
     */
    @GetMapping("all")
    fun getExercisesPage(model: Model): String {
        val exercises = exercisesService.getExercises(ExerciseSearchDto(), DEFAULT_PAGE_REQUEST)
        addExercisePageAttributes(model, exercises, exercisesService)
        return "ExercisesSearch"
    }

    /**
     * Отображение страницы при пагинации
     */
    @GetMapping
    fun getExercises(
        @RequestParam searchDto: ExerciseSearchDto?,
        @RequestParam(value = "pageSize", required = false, defaultValue = "10") pageSize: Int,
        @RequestParam(value = "pageNumber", required = false, defaultValue = "1") pageNumber: Int,
        model: Model
    ): String {
        val exercises = exercisesService.getExercises(
            searchDto ?: ExerciseSearchDto(),
            PageRequest.of(pageNumber - 1, pageSize)
        )
        addExercisePageAttributes(model, exercises, exercisesService)
        return "ExercisesSearch"
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
            searchDto,
            PageRequest.of(pageNumber - 1, pageSize)
        )
        model.addAttribute("exercises", exercises)
        return "ExercisesSearch :: exercises"
    }

    /**
     * Получение типов упражнений
     */
    @GetMapping("types")
    @ResponseBody
    fun getExerciseTypes(): List<ExerciseType> {
        return exercisesService.getExerciseTypes()
    }

    companion object {
        private val DEFAULT_PAGE_REQUEST = PageRequest.of(0, 10)
    }
}
