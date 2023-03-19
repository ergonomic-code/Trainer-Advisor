package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.dtos.ImageDto
import nsu.fit.qyoga.core.questionnaires.api.model.Image

interface ImageService {
    fun uploadImage(image: ImageDto): Long

    fun getImage(id: Long): Image?
}