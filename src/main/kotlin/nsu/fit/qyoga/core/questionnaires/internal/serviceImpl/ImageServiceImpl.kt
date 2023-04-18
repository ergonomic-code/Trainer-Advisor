package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.questionnaires.api.errors.ImageException
import nsu.fit.qyoga.core.questionnaires.api.model.Image
import nsu.fit.qyoga.core.questionnaires.api.services.ImageService
import nsu.fit.qyoga.core.questionnaires.internal.repository.ImageJdbcTemplateRepo
import nsu.fit.qyoga.core.questionnaires.internal.repository.ImageRepo
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ImageServiceImpl(
    private val imageTemplateRepo: ImageJdbcTemplateRepo,
    private val imageRepo: ImageRepo
) : ImageService {
    override fun uploadImage(file: MultipartFile): Long {
        return imageTemplateRepo.save(
            Image(
                name = file.originalFilename ?: throw ImageException("Ошибка при сохранении: ошибка в имени файла"),
                mediaType = file.contentType ?: "application/octet-stream",
                size = file.size,
                data = file.bytes
            )
        )
    }

    override fun deleteImage(id: Long) {
        imageRepo.deleteById(id)
    }

    override fun getImage(id: Long): Image {
        return imageRepo.findById(id).orElse(null)
            ?: throw ImageException("Выбранное изображение не найдено")
    }
}
