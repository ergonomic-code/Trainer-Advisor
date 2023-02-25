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
        val exercises = exercisesService.getExercises(null, null, null, null, null, DEFAULT_PAGE_REQUEST)
        addExercisePageAttributes(model, exercises, exercisesService)
        return "ExercisesSearch"
    }

    /**
     * Отображение страницы при пагинации
     */
    @GetMapping
    fun getExercises(
        @RequestParam(value = "title", required = false) title: String?,
        @RequestParam(value = "contraindication", required = false) contradiction: String?,
        @RequestParam(value = "duration", required = false) duration: String?,
        @RequestParam(value = "exerciseType", required = false) exerciseType: ExerciseType?,
        @RequestParam(value = "therapeuticPurpose", required = false) therapeuticPurpose: String?,
        @RequestParam(value = "pageSize", required = false, defaultValue = "10") pageSize: Int,
        @RequestParam(value = "pageNumber", required = false, defaultValue = "1") pageNumber: Int,
        model: Model
    ): String {
        val exercises = exercisesService.getExercises(
            title,
            contradiction,
            duration,
            exerciseType,
            therapeuticPurpose,
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
            searchDto.title,
            searchDto.contradiction,
            searchDto.duration,
            searchDto.exerciseType,
            searchDto.therapeuticPurpose,
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
