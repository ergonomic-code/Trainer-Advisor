package pro.qyoga.core.appointments.types.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.spring.sdj.erpo.hydration.Identifiable
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.Instant


typealias AppointmentTypeRef = AggregateReference<AppointmentType, Long>

@Table("appointment_types")
data class AppointmentType(
    val ownerRef: TherapistRef,
    val name: String,

    @Id
    override val id: Long = 0,
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val modifiedAt: Instant? = null,
    @Version
    val version: Long = 0
) : Identifiable<Long>