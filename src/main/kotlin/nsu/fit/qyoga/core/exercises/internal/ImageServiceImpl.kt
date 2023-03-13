package nsu.fit.qyoga.core.exercises.internal

import nsu.fit.qyoga.core.exercises.api.ImagesService
import nsu.fit.qyoga.core.exercises.api.dtos.ImageDto
import nsu.fit.qyoga.core.exercises.api.model.Image
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
