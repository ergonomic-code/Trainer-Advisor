package pro.qyoga.core.users.therapists

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.spring.sdj.ergo.hydration.Identifiable
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import java.time.Instant
import java.util.*

typealias TherapistRef = AggregateReference<Therapist, UUID>

@Table("therapists")
data class Therapist(
    val firstName: String,
    val lastName: String,

    @Id
    override val id: UUID,
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val modifiedAt: Instant? = null,
    @Version
    val version: Long = 0
) : Identifiable<UUID>

val QyogaUserDetails.ref: TherapistRef
    get() = AggregateReference.to(id)
