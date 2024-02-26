package pro.qyoga.tests.fixture.backgrounds

import org.springframework.data.domain.Page
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.stereotype.Component
import pro.azhidkov.platform.file_storage.api.StoredFile
import pro.azhidkov.platform.spring.sdj.ALL
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.core.clients.cards.api.Client
import pro.qyoga.core.clients.files.ClientFilesService
import pro.qyoga.core.clients.files.model.ClientFile
import pro.qyoga.tests.fixture.object_mothers.FilesObjectMother

@Component
class ClientFilesBackgrounds(private val clientFilesService: ClientFilesService) {

    fun findAll(clientId: Long): Page<ClientFile> =
        clientFilesService.findFilesPage(clientId, pageRequest = ALL)

    fun findFile(clientId: Long, file: StoredFile): ClientFile? {
        return findAll(clientId).find { it.fileRef.resolveOrThrow().name == file.metaData.name }
    }

    fun createFiles(clientId: Long, filesCount: Int): List<ClientFile> {
        return (1..filesCount).map {
            createFile(clientId, FilesObjectMother.randomFile())
        }
    }

    fun createFile(clientId: Long, file: StoredFile = FilesObjectMother.randomFile()): ClientFile {
        val clientRef = AggregateReference.to<Client, Long>(clientId)
        return clientFilesService.addFile(clientRef, file)
    }

}
