package pro.qyoga.core.clients.cards.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Embedded
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.spring.sdj.ergo.hydration.Identifiable
import pro.azhidkov.platform.uuid.UUIDv7
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.Instant
import java.time.LocalDate
import java.util.*

typealias ClientRef = AggregateReference<Client, UUID>

@Table("clients")
data class Client(
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val birthDate: LocalDate?,
    val phoneNumber: String,
    val email: String?,
    val address: String?,
    val complaints: String?,
    val anamnesis: String?,
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL, prefix = "distribution_source_")
    val distributionSource: DistributionSource?,
    val therapistRef: TherapistRef,

    @Id
    override val id: UUID = UUIDv7.randomUUID(),
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val modifiedAt: Instant? = null,
    @Version
    val version: Long = 0
) : Identifiable<UUID> {

    fun fullName() = listOf(lastName, firstName, middleName)
        .filter { it?.isNotBlank() == true }
        .joinToString(" ")

}
