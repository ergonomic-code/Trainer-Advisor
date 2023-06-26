package nsu.fit.qyoga.core.questionnaires.api.dtos

import nsu.fit.qyoga.core.clients.api.Dto.ClientSearchDto

data class GenerateLinkSearchClientsDto(
    val clientName: String = ""
)

fun GenerateLinkSearchClientsDto.toClientSearchDto(): ClientSearchDto {
    val fullName = this.clientName.split(" ").toTypedArray()
    return ClientSearchDto(
        lastName = fullName.getOrNull(0),
        firstName = fullName.getOrNull(1),
        patronymic = fullName.getOrNull(2)
    )
}
