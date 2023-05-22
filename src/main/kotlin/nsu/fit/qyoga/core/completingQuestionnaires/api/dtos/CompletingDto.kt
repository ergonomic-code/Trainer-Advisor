package nsu.fit.qyoga.core.completingQuestionnaires.api.dtos

import java.util.Date

data class CompletingDto(
    var id: Long,
    var questionnaire: CompletingQuestionnaireDto,
    var client: CompletingClientDto,
    var completingDate: Date,
    var numericResult: Long,
    var textResult: String,
)
