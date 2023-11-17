package pro.qyoga.core.clients.api

import java.time.LocalDate

data class ClientDto(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthDate: LocalDate,
    val phoneNumber: String,
    val email: String?,
    val areaOfResidence: String?,
    val distributionSource: String?,
    val complains: String,
    val id: Long = 0
)
