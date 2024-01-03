package pro.qyoga.platform.file_storage.api


interface FilesStorage {

    fun uploadFile(file: File): Long

}
