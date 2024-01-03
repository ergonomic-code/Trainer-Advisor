package pro.qyoga.platform.file_storage.internal

import org.springframework.stereotype.Service
import pro.qyoga.platform.file_storage.api.Image
import pro.qyoga.platform.file_storage.api.ImagesService

@Service
class ImagesServiceImpl(
    private val imagesRepo: ImagesRepo
) : ImagesService {

    override fun uploadImage(image: Image): Long {
        return imagesRepo.save(image).id
    }

}