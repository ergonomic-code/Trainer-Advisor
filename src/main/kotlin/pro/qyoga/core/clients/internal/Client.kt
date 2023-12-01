package pro.qyoga.core.clients.internal

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Table
import pro.qyoga.core.clients.api.CreateClientRequest
import pro.qyoga.core.users.api.Therapist
import java.time.Instant
import java.time.LocalDate


@Table("clients")
data class Client(
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val birthDate: LocalDate,
    val phoneNumber: String,
    val email: String?,
    val address: String?,
    val distributionSource: String?,
    val complaints: String,
    val therapistId: AggregateReference<Therapist, Long>,

    @Id
    val id: Long = 0,
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val modifiedAt: Instant? = null,
    @Version
    val version: Long = 0
) {

    constructor(therapistId: Long, createRequest: CreateClientRequest) :
            this(
                createRequest.firstName,
                createRequest.lastName,
                createRequest.middleName,
                createRequest.birthDate,
                createRequest.phoneNumber,
                createRequest.email,
                createRequest.address,
                createRequest.distributionSource,
                createRequest.complaints,
                AggregateReference.to(therapistId)
            )

}