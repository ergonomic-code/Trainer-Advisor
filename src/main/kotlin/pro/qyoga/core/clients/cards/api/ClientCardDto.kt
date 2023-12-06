package pro.qyoga.core.clients.cards.api

import org.springframework.format.annotation.DateTimeFormat
import pro.qyoga.core.formats.russianDateFormatPattern
import java.time.LocalDate

data class ClientCardDto(
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    @DateTimeFormat(pattern = russianDateFormatPattern)
    val birthDate: LocalDate?,
    val phoneNumber: String,
    val email: String?,
    val address: String?,
    val complaints: String,
    val anamnesis: String?,
    val distributionSourceType: DistributionSourceType?,
    val distributionSourceComment: String?,
) {

    val distributionSource = distributionSourceType?.let { DistributionSource(it, distributionSourceComment) }

}
