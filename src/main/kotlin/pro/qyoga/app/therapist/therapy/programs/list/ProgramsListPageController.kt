package pro.qyoga.app.therapist.therapy.programs.list

import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.app.platform.ResponseEntityExt
import pro.qyoga.core.therapy.programs.impl.ProgramsRepo
import pro.qyoga.core.therapy.programs.dtos.ProgramsSearchFilter
import pro.qyoga.core.therapy.programs.impl.findAllMatching


private const val PROGRAMS_LIST_VIEW = "therapist/therapy/programs/programs-list.html"

private const val PROGRAMMS_ATTR = "programs"

@Controller
@RequestMapping("/therapist/programs")
class ProgramsListPageController(
    private val programsRepo: ProgramsRepo,
    private val generateProgramDocx: GenerateProgramDocx
) {

    @GetMapping
    fun getProgramsListPage(): ModelAndView {
        val firstPage = programsRepo.findAll(ProgramsRepo.Page.firstTenByTitle)

        return modelAndView(PROGRAMS_LIST_VIEW) {
            PROGRAMMS_ATTR bindTo firstPage
        }
    }

    @GetMapping("{programId}/docx")
    fun getProgramDocx(@PathVariable programId: Long): ResponseEntity<InputStreamResource> {
        val docxInputStream = generateProgramDocx(programId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntityExt.ok(docxInputStream)
    }

    @DeleteMapping("/{programId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteProgram(@PathVariable programId: Long) {
        programsRepo.deleteById(programId)
    }

    @GetMapping("/search")
    fun searchPrograms(
        @ModelAttribute programsSearchFilter: ProgramsSearchFilter
    ): ModelAndView {
        val searchResult = programsRepo.findAllMatching(programsSearchFilter, ProgramsRepo.Page.firstTenByTitle)

        return modelAndView("$PROGRAMS_LIST_VIEW :: programsTable") {
            PROGRAMMS_ATTR bindTo searchResult
        }
    }

}