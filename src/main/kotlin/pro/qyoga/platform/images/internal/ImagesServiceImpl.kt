package pro.qyoga.platform.images.internal

import org.springframework.stereotype.Service
import pro.qyoga.platform.images.api.Image
import pro.qyoga.platform.images.api.ImagesService

@Service
class ImagesServiceImpl(
    private val imagesRepo: ImagesRepo
) : ImagesService {

    override fun uploadImage(image: Image): Long {
        return imagesRepo.save(image).id
    }

}