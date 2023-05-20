package nsu.fit.qyoga.core.completingQuestionnaires.api.dtos

import nsu.fit.qyoga.core.clients.api.Dto.ClientDto
import java.util.Date

data class CompletingDto(
    var completingId: Long,
    var client: ClientDto,
    var completingDate: Date,
    var numericResult: Long,
    var textResult: String,
)
