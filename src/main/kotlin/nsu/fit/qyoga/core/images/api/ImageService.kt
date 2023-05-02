package nsu.fit.qyoga.core.images.api

import nsu.fit.qyoga.core.images.api.model.Image
import org.springframework.web.multipart.MultipartFile

interface ImageService {
    fun uploadImage(file: MultipartFile): Long
    fun deleteImage(id: Long)
    fun getImage(id: Long): Image
    fun getImageList(idList: List<Long>): List<Image>
}
