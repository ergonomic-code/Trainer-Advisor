package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.questionnaires.api.dtos.ImageDto
import nsu.fit.qyoga.core.questionnaires.api.model.Image
import nsu.fit.qyoga.core.questionnaires.api.services.ImageService
import nsu.fit.qyoga.core.questionnaires.internal.repository.ImageRepo
import org.springframework.stereotype.Service

@Service
class ImageServiceImpl(
    private val imageRepo: ImageRepo
) :ImageService {
    override fun uploadImage(image: ImageDto): Long {
        return imageRepo.save(
            Image(
                name = image.name ?: "",
                mediaType = image.mediaType,
                size = image.size,
                data = image.data.readAllBytes()
            )
        ).id
    }

    override fun getImage(id: Long): Image? {
        return imageRepo.findById(id).map { it }.orElse(null)
    }
}