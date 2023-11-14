package pro.qyoga.infra.files.api


interface FilesService {

    fun uploadFile(file: File): Long

}
