package nsu.fit.qyoga.core.completingQuestionnaires.api.dtos

import java.time.Instant
import java.util.Date

data class CompletingDto(
    var completingId: Long = 0,
    var client: ClientDto = ClientDto(userId = 0, firstName = "firstName", lastName = "lastName"),
    var completingDate: Date = Date.from(Instant.now()),
    var numericResult: Long = 0,
    var textResult: String = "",
)
