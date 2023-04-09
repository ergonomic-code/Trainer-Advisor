package nsu.fit.qyoga.core.questionnaires.api.services

import nsu.fit.qyoga.core.questionnaires.api.dtos.DecodingDto

interface DecodingService {
    fun createNewDecoding(questionnaireId: Long): DecodingDto

    fun findDecodingByQuestionnaireId(questionnaireId: Long): List<DecodingDto>
}