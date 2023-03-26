package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.model.Image

interface ImageService {
    fun uploadImage(image: Image): Long

    fun getImage(id: Long): Image?
}