package pro.qyoga.core.clients.cards.dtos

import org.springframework.format.annotation.DateTimeFormat
import pro.qyoga.core.clients.cards.model.DistributionSource
import pro.qyoga.core.clients.cards.model.DistributionSourceType
import pro.qyoga.l10n.RUSSIAN_DATE_FORMAT_PATTERN
import java.time.LocalDate

data class ClientCardDto(
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    @DateTimeFormat(pattern = RUSSIAN_DATE_FORMAT_PATTERN)
    val birthDate: LocalDate?,
    val phoneNumber: String,
    val email: String?,
    val address: String?,
    val complaints: String?,
    val anamnesis: String?,
    val distributionSourceType: DistributionSourceType?,
    val distributionSourceComment: String?,
) {

    val distributionSource = distributionSourceType?.let { DistributionSource(it, distributionSourceComment) }

}