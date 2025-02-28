package pro.qyoga.app.therapist.therapy.programs.list

import org.springframework.core.io.InputStreamResource
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.viewId
import pro.qyoga.app.platform.ResponseEntityExt
import pro.qyoga.core.therapy.programs.ProgramsRepo
import pro.qyoga.core.therapy.programs.dtos.ProgramsSearchFilter
import pro.qyoga.core.therapy.programs.findAllMatching
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref


data class ProgramsListPageModel(
    val programs: Page<Program>,
    val fragment: String? = null
) : ModelAndView(
    viewId("therapist/therapy/programs/programs-list", fragment), mapOf(
        "programs" to programs,
    )
)

@Controller
@RequestMapping("/therapist/programs")
class ProgramsListPageController(
    private val programsRepo: ProgramsRepo,
    private val generateProgramDocx: GenerateProgramDocx
) {

    @GetMapping
    fun getProgramsListPage(
        @AuthenticationPrincipal principal: QyogaUserDetails
    ): ProgramsListPageModel {
        val firstPage = programsRepo.findPage(ProgramsRepo.Page.firstTenByTitle) {
            Program::ownerRef isEqual principal.ref
        }

        return ProgramsListPageModel(firstPage)
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
        @ModelAttribute programsSearchFilter: ProgramsSearchFilter,
        @AuthenticationPrincipal principal: QyogaUserDetails
    ): ProgramsListPageModel {
        val searchResult = programsRepo.findAllMatching(programsSearchFilter, ProgramsRepo.Page.firstTenByTitle)

        return ProgramsListPageModel(searchResult, "programsTable")
    }

}