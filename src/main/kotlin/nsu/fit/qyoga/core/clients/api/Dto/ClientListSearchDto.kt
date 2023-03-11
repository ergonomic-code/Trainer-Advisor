package nsu.fit.qyoga.core.clients.api.Dto

import java.util.*

data class ClientListSearchDto(
    val name: String? = null,
    val secondname: String? = null,
    val surname: String? = null,
    val phone_number: Long? = null,
    val last_appointment: Date? = null,
    val diagnose: String? = null
)
