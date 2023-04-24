package nsu.fit.qyoga.core.completingQuestionnaires.api.dtos

import java.util.Date

data class CompletingDto(
    var completingId: Long,
    var client: ClientDto,
    var completingDate: Date,
    var numericResult: Long,
    var textResult: String,
)
