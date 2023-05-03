package nsu.fit.qyoga.core.images.internal

import nsu.fit.qyoga.core.images.api.ImageService
import nsu.fit.qyoga.core.images.api.error.ImageException
import nsu.fit.qyoga.core.images.api.model.Image
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ImageServiceImpl(
    private val imageRepo: ImageRepo,
    private val imageJdbcTemplateRepo: ImageJdbcTemplateRepo
) : ImageService {
    override fun uploadImage(file: MultipartFile): Long {
        return imageRepo.save(
            Image(
                name = file.originalFilename ?: "",
                mediaType = file.contentType ?: "application/octet-stream",
                size = file.size,
                data = file.bytes
            )
        ).id
    }

    override fun deleteImage(id: Long) {
        imageRepo.deleteById(id)
    }

    override fun getImage(id: Long): Image {
        return imageRepo.findByIdOrNull(id) ?: throw ImageException("No existing image with id = $id")
    }

    override fun getImageList(idList: List<Long>): List<Image> {
        return imageJdbcTemplateRepo.findImageList(idList)
    }
}
