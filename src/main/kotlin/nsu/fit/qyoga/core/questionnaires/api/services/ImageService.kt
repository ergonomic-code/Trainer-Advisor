package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.model.Image
import org.springframework.web.multipart.MultipartFile

interface ImageService {
    fun uploadImage(file: MultipartFile): Long
    fun deleteImage(id: Long)
    fun getImage(id: Long): Image
}
