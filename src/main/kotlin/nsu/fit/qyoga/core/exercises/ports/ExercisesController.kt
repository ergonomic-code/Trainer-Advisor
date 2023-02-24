package nsu.fit.qyoga.core.exercises.ports

import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.util.stream.Collectors
import java.util.stream.IntStream

@Controller
@RequestMapping("/exercises/")
class ExercisesController(
    private val exercisesService: ExercisesService
) {
    companion object {
        private val DEFAULT_PAGE_REQUEST = PageRequest.of(0, 10)
    }

    /**
     * Отображение страницы со списком упражнений
     */
    @GetMapping("all")
    fun getExercisesPage(model: Model): String {
        val types = exercisesService.getExerciseTypes()
        val exercises = exercisesService.getExercises(null, null, null, null, null, DEFAULT_PAGE_REQUEST)
        model.addAttribute("types", types)
        model.addAttribute("exercises", exercises)
        model.addAttribute(
            "pageNumbers",
            IntStream.rangeClosed(1, exercises.totalPages).boxed().collect(Collectors.toList())
        )
        return "ExercisesSearch"
    }

    /**
     * Получение списка упражнений
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
        model.addAttribute("exercises", exercises)
        model.addAttribute(
            "pageNumbers",
            IntStream.rangeClosed(1, exercises.totalPages).boxed().collect(Collectors.toList())
        )
        return "ExercisesSearch"
    }

    /**
     * Получение типов упражнений
     */
    @GetMapping("types")
    @ResponseBody
    fun getExerciseTypes(): List<ExerciseType> {
        return exercisesService.getExerciseTypes()
    }
}
