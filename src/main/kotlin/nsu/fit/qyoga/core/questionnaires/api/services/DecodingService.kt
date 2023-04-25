package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.dtos.DecodingDto

interface DecodingService {
    fun createNewDecoding(questionnaireId: Long): DecodingDto
    fun deleteDecodingById(id: Long)
    fun saveDecoding(decoding: DecodingDto): DecodingDto
    fun saveDecodingList(decodingList: List<DecodingDto>): List<DecodingDto>
    fun findDecodingByQuestionnaireId(questionnaireId: Long): MutableList<DecodingDto>
}
