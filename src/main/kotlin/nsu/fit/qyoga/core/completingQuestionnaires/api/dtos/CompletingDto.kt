package nsu.fit.qyoga.core.completingQuestionnaires.api.dtos

import java.time.LocalDate

data class CompletingDto(
    val id: Long,
    val questionnaire: CompletingQuestionnaireDto,
    val client: CompletingClientDto,
    val completingDate: LocalDate,
    val numericResult: Long,
    val textResult: String,
)
