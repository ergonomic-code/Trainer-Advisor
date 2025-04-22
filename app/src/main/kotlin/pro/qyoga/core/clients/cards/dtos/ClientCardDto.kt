package pro.qyoga.core.clients.cards.dtos

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.format.annotation.DateTimeFormat
import pro.qyoga.core.clients.cards.model.DistributionSource
import pro.qyoga.core.clients.cards.model.DistributionSourceType
import pro.qyoga.core.clients.cards.model.HasPersonName
import pro.qyoga.l10n.RUSSIAN_DATE_FORMAT_PATTERN
import java.time.LocalDate

data class ClientCardDto(
    override val firstName: String,
    override val lastName: String,
    override val middleName: String?,
    @DateTimeFormat(pattern = RUSSIAN_DATE_FORMAT_PATTERN)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = RUSSIAN_DATE_FORMAT_PATTERN)
    val birthDate: LocalDate?,
    val phoneNumber: String,
    val email: String?,
    val address: String?,
    val complaints: String?,
    val anamnesis: String?,
    val distributionSourceType: DistributionSourceType?,
    val distributionSourceComment: String?,
    val version: Long
) : HasPersonName {

    val distributionSource = distributionSourceType?.let { DistributionSource(it, distributionSourceComment) }

}