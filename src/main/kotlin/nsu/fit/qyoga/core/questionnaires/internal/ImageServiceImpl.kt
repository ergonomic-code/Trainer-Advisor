package nsu.fit.qyoga.core.questionnaires.internal

import nsu.fit.qyoga.core.questionnaires.api.dtos.ImageDto
import nsu.fit.qyoga.core.questionnaires.api.model.Image
import nsu.fit.qyoga.core.questionnaires.api.services.ImagesService
import org.springframework.stereotype.Service

@Service
class ImageServiceImpl(
    private val imagesRepo: ImagesRepo
) : ImagesService {
    override fun uploadImage(image: ImageDto): Long {
        return imagesRepo.save(
            Image(
                name = image.name ?: "",
                mediaType = image.mediaType,
                size = image.size,
                data = image.data.readAllBytes()
            )
        ).id
    }

    override fun getImage(id: Long): Image? {
        return imagesRepo.findById(id).map { it }.orElse(null)
    }
}