package nsu.fit.qyoga.core.images.api

import nsu.fit.qyoga.core.images.api.model.Image

interface ImageService {
    fun uploadImage(image: Image): Long
    fun deleteImage(id: Long): Image?
    fun getImage(id: Long): Image?
    fun getImageList(idList: List<Long>): List<Image>
}
