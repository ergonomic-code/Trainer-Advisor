package nsu.fit.qyoga.core.exercises.api

import nsu.fit.qyoga.core.exercises.api.dtos.ImageDto
import nsu.fit.qyoga.core.exercises.api.model.Image

interface ImagesService {
    fun uploadImage(image: ImageDto): Long

    fun getImage(id: Long): Image?
}
