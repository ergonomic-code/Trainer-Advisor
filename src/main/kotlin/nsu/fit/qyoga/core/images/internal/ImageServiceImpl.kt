package nsu.fit.qyoga.core.images.internal

import nsu.fit.qyoga.core.images.api.ImageService
import nsu.fit.qyoga.core.images.api.model.Image
import org.springframework.stereotype.Service

@Service
class ImageServiceImpl(
    private val imageJdbcTemplateRepo: ImageJdbcTemplateRepo
) : ImageService {
    override fun uploadImage(image: Image): Long {
        return imageJdbcTemplateRepo.save(image)
    }

    override fun deleteImage(id: Long): Image? {
        val image = imageJdbcTemplateRepo.findById(id)
        if (image != null) {
            imageJdbcTemplateRepo.delete(id)
        }
        return image
    }

    override fun getImage(id: Long): Image? {
        return imageJdbcTemplateRepo.findById(id)
    }

    override fun getImageList(idList: List<Long>): List<Image> {
        return imageJdbcTemplateRepo.findImageList(idList)
    }
}
