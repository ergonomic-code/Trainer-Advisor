package nsu.fit.qyoga.core.exercises.ports

import nsu.fit.platform.web.pages.Page
import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/exercises/")
class ExercisesController(
    private val exercisesService: ExercisesService
) {

    /**
     * Отображение страницы со списком упражнений
     */
    @GetMapping("all")
    fun getExercisesPage(model: Model): String {
        val types = exercisesService.getExerciseTypes()
        model.addAttribute("types", types)
        return "ExercisesSearch"
    }

    /**
     * Получение списка упражнений
     */
    @GetMapping
    @ResponseBody
    fun getExercises(
        @RequestParam(value = "title", required = false) title: String?,
        @RequestParam(value = "contraindication", required = false) contradiction: String?,
        @RequestParam(value = "duration", required = false) duration: String?,
        @RequestParam(value = "exerciseType", required = false) exerciseType: ExerciseType?,
        @RequestParam(value = "therapeuticPurpose", required = false) therapeuticPurpose: String?,
        @RequestParam(value = "pageSize", required = false, defaultValue = "10") pageSize: Int,
        @RequestParam(value = "pageIndex", required = false, defaultValue = "1") pageIndex: Int
    ): Page<ExerciseDto> {
        return exercisesService.getExercises(
            title,
            contradiction,
            duration,
            exerciseType,
            therapeuticPurpose,
            PageRequest.of(pageIndex, pageSize)
        )
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
