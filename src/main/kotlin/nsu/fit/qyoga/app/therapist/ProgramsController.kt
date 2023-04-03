package nsu.fit.qyoga.app.therapist

import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.programs.api.ProgramsService
import nsu.fit.qyoga.core.programs.api.dtos.ProgramDto
import nsu.fit.qyoga.core.programs.api.dtos.ProgramSearchDto
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
@RequestMapping("/programs/")
class ProgramsController(
    private val programsService: ProgramsService
) {
    /**
     * Отображение страницы со списком программ
     */
    @GetMapping
    fun getPrograms(
        @ModelAttribute("searchDto") searchDto: ProgramSearchDto,
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
        @ModelAttribute("searchDto") searchDto: ProgramSearchDto,
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

    fun addProgramsPageAttributes(model: Model, programs: Page<ProgramDto>) {
        model.addAttribute("searchDto", ExerciseSearchDto())
        model.addAttribute("programs", programs)
        model.addAttribute(
            "pageNumbers",
            IntStream.rangeClosed(1, programs.totalPages).boxed().collect(Collectors.toList())
        )
    }
}
