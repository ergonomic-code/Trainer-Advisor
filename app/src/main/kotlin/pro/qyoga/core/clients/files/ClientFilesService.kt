package pro.qyoga.core.clients.files

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pro.azhidkov.platform.file_storage.api.FilesStorage
import pro.azhidkov.platform.file_storage.api.StoredFile
import pro.azhidkov.platform.file_storage.api.StoredFileInputStream
import pro.azhidkov.platform.spring.sdj.ergo.hydration.AggregateReferenceTarget
import pro.qyoga.core.clients.cards.model.ClientRef
import pro.qyoga.core.clients.files.impl.ClientFilesRepo
import pro.qyoga.core.clients.files.impl.findClientFilesPage
import pro.qyoga.core.clients.files.impl.findFile
import pro.qyoga.core.clients.files.model.ClientFile
import java.util.*


@Service
class ClientFilesService(
    private val clientFilesRepo: ClientFilesRepo,
    private val clientFilesStorage: FilesStorage,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun addFile(clientRef: ClientRef, file: StoredFile): ClientFile {
        val persistedFile = clientFilesStorage.uploadFile(file)
        val clientFile = ClientFile(clientRef, AggregateReferenceTarget(persistedFile))
        return clientFilesRepo.save(clientFile)
    }

    fun findFilesPage(clientId: UUID, pageRequest: PageRequest = ClientFilesRepo.Page.tenNewest): Page<ClientFile> {
        return clientFilesRepo.findClientFilesPage(clientId, pageRequest)
    }

    fun findFileContent(clientId: UUID, fileId: Long): StoredFileInputStream? {
        val clientFile = clientFilesRepo.findFile(clientId, fileId)
            ?: return null

        return clientFilesStorage.findByIdOrNull(clientFile.fileRef.id!!)
    }

    fun deleteFile(clientId: UUID, fileId: Long): ClientFile? {
        val clientFile = clientFilesRepo.findFile(clientId, fileId)
            ?: return null

        clientFilesRepo.deleteById(clientFile.id)

        try {
            clientFilesStorage.deleteById(clientFile.fileRef.id!!)
        } catch (ex: Exception) {
            log.warn("Client file deletion failed", ex)
        }

        return clientFile
    }

}