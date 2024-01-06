package pro.qyoga.platform.file_storage.internal

import pro.qyoga.platform.file_storage.api.File
import pro.qyoga.platform.file_storage.api.FilesStorage

class FilesStorageImpl(
    private val filesRepo: FilesRepo,
    private val bucket: String
) : FilesStorage {

    override fun uploadFile(file: File): Long {
        return filesRepo.save(file.atBucket(bucket)).id
    }

    override fun uploadAll(files: Iterable<File>): Iterable<File> {
        return filesRepo.saveAll(files)
    }

}