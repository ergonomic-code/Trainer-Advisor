package pro.qyoga.tests.fixture.backgrounds

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import pro.azhidkov.platform.file_storage.api.StoredFile
import pro.azhidkov.platform.spring.sdj.ALL
import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.model.ClientRef
import pro.qyoga.core.clients.files.ClientFilesService
import pro.qyoga.core.clients.files.model.ClientFile
import pro.qyoga.tests.fixture.object_mothers.FilesObjectMother
import java.util.*

@Component
class ClientFilesBackgrounds(private val clientFilesService: ClientFilesService) {

    fun findAll(clientId: UUID): Page<ClientFile> =
        clientFilesService.findFilesPage(clientId, pageRequest = ALL)

    fun findFile(clientId: UUID, file: StoredFile): ClientFile? {
        return findAll(clientId).find { it.fileRef.resolveOrThrow().name == file.metaData.name }
    }

    fun createFiles(clientId: UUID, filesCount: Int): List<ClientFile> {
        return (1..filesCount).map {
            createFile(clientId, FilesObjectMother.randomFile())
        }
    }

    fun createFile(clientId: UUID, file: StoredFile = FilesObjectMother.randomFile()): ClientFile {
        val clientRef = ClientRef.to<Client, UUID>(clientId)
        return clientFilesService.addFile(clientRef, file)
    }

}
