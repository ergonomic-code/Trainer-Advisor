package pro.qyoga.platform.file_storage.internal

import org.springframework.stereotype.Service
import pro.qyoga.platform.file_storage.api.File
import pro.qyoga.platform.file_storage.api.FilesStorage

@Service
class FilesStorageImpl(
    private val filesRepo: FilesRepo
) : FilesStorage {

    override fun uploadFile(file: File): Long {
        return filesRepo.save(file).id
    }

}