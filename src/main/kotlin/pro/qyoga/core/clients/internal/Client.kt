package pro.qyoga.core.clients.internal

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import pro.qyoga.core.clients.api.ClientDto
import java.time.Instant
import java.time.LocalDate


@Table("clients")
data class Client(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val birthDate: LocalDate,
    val phoneNumber: String,
    val email: String?,
    val areaOfResidence: String?,
    val distributionSource: String?,
    val complains: String,

    @Id
    val id: Long = 0,
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val modifiedAt: Instant? = null,
    @Version
    val version: Long = 0
) {

    constructor(dto: ClientDto) :
            this(dto.firstName, dto.lastName, dto.middleName, dto.birthDate, dto.phoneNumber, dto.email, dto.areaOfResidence, dto.distributionSource, dto.complains)

    fun toDto(): ClientDto = ClientDto(firstName, lastName, middleName, birthDate, phoneNumber, email, areaOfResidence, distributionSource, complains, id)

}