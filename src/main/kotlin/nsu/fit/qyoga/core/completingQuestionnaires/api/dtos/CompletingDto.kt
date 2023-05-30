package nsu.fit.qyoga.core.completingQuestionnaires.api.dtos

import java.util.Date

data class CompletingDto(
    val id: Long,
    val questionnaire: CompletingQuestionnaireDto,
    val client: CompletingClientDto,
    val completingDate: Date,
    val numericResult: Long,
    val textResult: String,
)
