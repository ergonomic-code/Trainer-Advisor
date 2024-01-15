package pro.qyoga.platform.file_storage.api


interface FilesStorage {

    fun uploadFile(file: StoredFile): FileMetaData

    fun uploadAll(files: Iterable<StoredFile>): Iterable<FileMetaData>

    fun findByIdOrNull(fileId: Long): StoredFileInputStream?

}
