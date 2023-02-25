package nsu.fit.qyoga.core.exercises.utils.view

import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.exercises.utils.pages.Page
import org.springframework.ui.Model
import java.util.stream.Collectors
import java.util.stream.IntStream

fun addExercisePageAttributes(model: Model, exercises: Page<ExerciseDto>, exercisesService: ExercisesService) {
    model.addAttribute("searchDto", ExerciseSearchDto())
    model.addAttribute("types", exercisesService.getExerciseTypes())
    model.addAttribute("exercises", exercises)
    model.addAttribute(
        "pageNumbers",
        IntStream.rangeClosed(1, exercises.totalPages).boxed().collect(Collectors.toList())
    )
}
