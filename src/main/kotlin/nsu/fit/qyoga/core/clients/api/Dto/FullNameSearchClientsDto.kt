package nsu.fit.qyoga.core.clients.api.Dto

data class FullNameClientsSearchDto(
    val clientName: String = ""
)

fun FullNameClientsSearchDto.toClientSearchDto(): ClientSearchDto {
    val fullName = this.clientName.split(" ").toTypedArray()
    return ClientSearchDto(
        lastName = fullName.getOrNull(0),
        firstName = fullName.getOrNull(1),
        patronymic = fullName.getOrNull(2)
    )
}
