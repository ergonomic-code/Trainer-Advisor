package pro.qyoga.core.clients.api

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class ClientCardDto(
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    val birthDate: LocalDate,
    val phoneNumber: String,
    val email: String?,
    val address: String?,
    val distributionSource: String?,
    val complaints: String,
)
