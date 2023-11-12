package pro.qyoga.infra.images.internal

import pro.qyoga.infra.images.api.Image
import pro.qyoga.infra.images.api.ImagesService

class ImagesServiceImpl(
    private val imagesRepo: ImagesRepo
) : ImagesService {

    override fun uploadImage(image: Image): Long {
        return imagesRepo.save(image).id
    }

}