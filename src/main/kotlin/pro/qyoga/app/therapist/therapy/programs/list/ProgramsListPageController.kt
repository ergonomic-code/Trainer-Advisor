package pro.qyoga.app.therapist.therapy.programs.list

import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import pro.qyoga.app.common.ResponseEntityExt
import pro.qyoga.core.therapy.programs.ProgramsRepo
import pro.qyoga.platform.spring.mvc.modelAndView


@Controller
@RequestMapping("/therapist/programs")
class ProgramsListPageController(
    private val programsRepo: ProgramsRepo,
    private val generateProgramDocx: GenerateProgramDocx
) {

    @GetMapping
    fun getProgramsListPage(): ModelAndView {
        return modelAndView("/therapist/therapy/programs/programs-list.html") {
            "programs" bindTo programsRepo.findAll(ProgramsRepo.Page.firstTenByTitle)
        }
    }

    @GetMapping("{programId}/docx")
    fun getProgramDocx(@PathVariable programId: Long): ResponseEntity<InputStreamResource> {
        val docxInputStream = generateProgramDocx(programId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntityExt.ok(docxInputStream)
    }

}