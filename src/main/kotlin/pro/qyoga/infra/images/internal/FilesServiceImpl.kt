package pro.qyoga.infra.images.internal

import pro.qyoga.infra.images.api.File
import pro.qyoga.infra.images.api.FilesService

class FilesServiceImpl(
    private val imagesRepo: ImagesRepo
) : FilesService {

    override fun uploadFile(file: File): Long {
        return imagesRepo.save(file).id
    }

}