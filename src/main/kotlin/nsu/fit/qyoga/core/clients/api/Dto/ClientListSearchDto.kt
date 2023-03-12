package nsu.fit.qyoga.core.clients.api.Dto

import java.util.*

data class ClientListSearchDto(
    val name: String? = null,
    val secondname: String? = null,
    val surname: String? = null,
    val phoneNumber: String? = null,
    val dateAppointment: Date? = null, //date-time
    val diagnose: String? = null
)
