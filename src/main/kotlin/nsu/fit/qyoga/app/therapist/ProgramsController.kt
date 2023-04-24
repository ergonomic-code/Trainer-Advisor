package nsu.fit.qyoga.app.therapist

import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.programs.api.ProgramsService
import nsu.fit.qyoga.core.programs.api.dtos.ProgramDto
import nsu.fit.qyoga.core.programs.api.dtos.ProgramSearchDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors
import java.util.stream.IntStream

@Controller
@RequestMapping("/programs/")
class ProgramsController(
    private val programsService: ProgramsService,
    private val exercisesService: ExercisesService
) {
    /**
     * Отображение страницы со списком программ
     */
    @GetMapping
    fun getPrograms(
        @ModelAttribute("searchProgramDto") searchDto: ProgramSearchDto,
        @RequestParam(value = "pageSize", required = false, defaultValue = "10") pageSize: Int,
        @RequestParam(value = "pageNumber", required = false, defaultValue = "1") pageNumber: Int,
        model: Model
    ): String {
        val programs = programsService.getPrograms(
            searchDto,
            PageRequest.of(pageNumber - 1, pageSize)
        )
        addProgramsPageAttributes(model, programs)
        return "programs/program-search"
    }

    /**
     * Фильтрация программ
     */
    @GetMapping("/search")
    fun getProgramsFiltered(
        @ModelAttribute("searchProgramDto") searchDto: ProgramSearchDto,
        @RequestParam(value = "pageSize", required = false, defaultValue = "10") pageSize: Int,
        @RequestParam(value = "pageNumber", required = false, defaultValue = "1") pageNumber: Int,
        model: Model
    ): String {
        val programs = programsService.getPrograms(
            searchDto,
            PageRequest.of(pageNumber - 1, pageSize)
        )
        addProgramsPageAttributes(model, programs)
        return "programs/program-search :: programs"
    }

    /**
     * Получение программы по id с упражнениями для просмотра терапевтом
     */
    @GetMapping("/{id}/view")
    fun getProgramViewWithExercises(
        @PathVariable id: Long,
        model: Model
    ): String {
        val program = programsService.getProgramById(id)
        model.addAttribute("program", program)
        return "programs/program-view"
    }

    /**
     * Получение программы по id с упражнениями для выполнения клиентом
     */
    @GetMapping("/{id}/exec")
    fun getProgramExecutionWithExercises(
        @PathVariable id: Long,
        model: Model
    ): String {
        val program = programsService.getProgramById(id)
        model.addAttribute("program", program)
        return "programs/program-execution"
    }

    /**
     * Создание программы
     */
    @PostMapping
    fun createProgram(
        model: Model
    ): String {
        // TODO: implement
        return "exercises/exercise-search"
    }

    /**
     * Сохранение программы в формате docx
     */
    @PostMapping("/{id}/download")
    fun downloadProgram(
        @PathVariable id: Long,
        model: Model
    ): String {
        val program = programsService.getProgramById(id)
        model.addAttribute("program", program)
        programsService.downloadProgram(program)
        return "programs/program-view"
    }

    /**
     * Получение страницы ручного волшебника
     */
    @GetMapping("/wizards/manual")
    fun getManualWizardPage(
        @ModelAttribute("searchDto") searchDto: ExerciseSearchDto,
        @RequestParam(value = "pageSize", required = false, defaultValue = "2") pageSize: Int,
        @RequestParam(value = "pageNumber", required = false, defaultValue = "1") pageNumber: Int,
        model: Model
    ): String {
        val exercises = exercisesService.getExercises(
            searchDto,
            PageRequest.of(pageNumber - 1, pageSize)
        )
        addExercisePageAttributes(model, exercises, exercisesService)
        return "wizards/manual-wizard"
    }

    /**
     * Получение страницы авто волшебника
     */
    @GetMapping("/wizards/auto")
    fun getAutoWizardPage(
        model: Model
    ): String {
        // TODO: implement
        return "wizards/auto-wizard"
    }

    fun addProgramsPageAttributes(model: Model, programs: Page<ProgramDto>) {
        model.addAttribute("searchProgramDto", ProgramSearchDto())
        model.addAttribute("programs", programs)
        model.addAttribute(
            "pageNumbers",
            IntStream.rangeClosed(1, programs.totalPages).boxed().collect(Collectors.toList())
        )
    }
}
