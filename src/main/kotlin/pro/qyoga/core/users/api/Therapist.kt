package pro.qyoga.core.users.api

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Table
import pro.qyoga.platform.spring.sdj.erpo.hydration.Identifiable
import java.time.Instant

typealias TherapistRef = AggregateReference<Therapist, Long>

@Table("therapists")
data class Therapist(
    val firstName: String,
    val lastName: String,

    @Id
    override val id: Long,
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val modifiedAt: Instant? = null,
    @Version
    val version: Long = 0
) : Identifiable<Long>