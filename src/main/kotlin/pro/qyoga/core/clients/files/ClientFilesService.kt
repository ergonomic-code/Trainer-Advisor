package pro.qyoga.core.clients.files

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.core.clients.cards.api.Client
import pro.qyoga.core.clients.files.impl.ClientFilesRepo
import pro.qyoga.core.clients.files.impl.findClientFilesPage
import pro.qyoga.core.clients.files.impl.findFile
import pro.qyoga.core.clients.files.model.ClientFile
import pro.qyoga.platform.file_storage.api.FilesStorage
import pro.qyoga.platform.file_storage.api.StoredFile
import pro.qyoga.platform.file_storage.api.StoredFileInputStream
import pro.qyoga.platform.spring.sdj.erpo.hydration.AggregateReferenceTarget


@Service
class ClientFilesService(
    private val clientFilesRepo: ClientFilesRepo,
    private val clientFilesStorage: FilesStorage,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun addFile(clientRef: AggregateReference<Client, Long>, file: StoredFile): ClientFile {
        val persistedFile = clientFilesStorage.uploadFile(file)
        val clientFile = ClientFile(clientRef, AggregateReferenceTarget(persistedFile))
        return clientFilesRepo.save(clientFile)
    }

    fun findFilesPage(clientId: Long, pageRequest: PageRequest = ClientFilesRepo.Page.tenNewest): Page<ClientFile> {
        return clientFilesRepo.findClientFilesPage(clientId, pageRequest)
    }

    fun findFileContent(clientId: Long, fileId: Long): StoredFileInputStream? {
        val clientFile = clientFilesRepo.findFile(clientId, fileId)
            ?: return null

        return clientFilesStorage.findByIdOrNull(clientFile.fileRef.id!!)
    }

    fun deleteFile(clientId: Long, fileId: Long): ClientFile? {
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