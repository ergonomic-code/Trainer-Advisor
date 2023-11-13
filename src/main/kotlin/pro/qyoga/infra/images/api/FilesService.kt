package pro.qyoga.infra.images.api


interface FilesService {

    fun uploadFile(file: File): Long

}
