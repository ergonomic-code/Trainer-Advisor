package nsu.fit.qyoga.core.clients.api.Dto

import java.time.LocalDate

data class ClientListSearchDto(
    val firstName: String? = null,
    val patronymic: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    val diagnose: String? = null,
    val appointmentDate: LocalDate? = null
)
