package pro.qyoga.app.therapist.clients.files

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.app.platform.ResponseEntityExt
import pro.qyoga.app.platform.notFound
import pro.qyoga.app.platform.toStoredFile
import pro.qyoga.app.therapist.clients.ClientPageTab
import pro.qyoga.app.therapist.clients.clientPageModel
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.cards.model.ClientRef
import pro.qyoga.core.clients.files.ClientFilesService
import pro.qyoga.core.clients.files.impl.ClientFilesRepo
import java.util.*


@Controller
@RequestMapping("/therapist/clients/{clientId}/files")
class ClientFilesPageController(
    private val clientsRepo: ClientsRepo,
    private val clientFilesService: ClientFilesService
) {

    @GetMapping
    fun getClientFilesPage(@PathVariable clientId: UUID): ModelAndView {
        val client = clientsRepo.findByIdOrNull(clientId)
            ?: return notFound

        val files = clientFilesService.findFilesPage(clientId, ClientFilesRepo.Page.tenNewest)

        return clientPageModel(client, ClientPageTab.FILES) {
            "clientFiles" bindTo files.content
        }
    }

    @PostMapping
    fun uploadImage(
        @PathVariable clientId: UUID,
        newFile: MultipartFile
    ): ModelAndView {
        val storedFile = newFile.toStoredFile()
        val uploadedFile = clientFilesService.addFile(ClientRef.to(clientId), storedFile)

        return modelAndView("therapist/clients/client-files-fragment :: client-files-list-row") {
            "clientFiles" bindTo listOf(uploadedFile)
        }
    }

    @GetMapping("/{fileId}")
    fun getFile(@PathVariable clientId: UUID, @PathVariable fileId: Long): Any {
        val storedFileInputStream = clientFilesService.findFileContent(clientId, fileId)
            ?: return notFound

        return ResponseEntityExt.ok(storedFileInputStream)
    }

    @DeleteMapping("/{fileId}")
    fun deleteFile(@PathVariable clientId: UUID, @PathVariable fileId: Long): ResponseEntity<String> {
        clientFilesService.deleteFile(clientId, fileId)
        return ResponseEntity.ok(null)
    }

}