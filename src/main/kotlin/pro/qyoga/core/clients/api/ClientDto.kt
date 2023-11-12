package pro.qyoga.core.clients.api

import java.time.LocalDate

data class ClientDto(
    val firstName: String,
    val lastName: String,
    val patronymic: String,
    val birthDate: LocalDate,
    val phoneNumber: String,
    val email: String?,
    val id: Long = 0
)
