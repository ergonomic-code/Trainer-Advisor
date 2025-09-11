package pro.qyoga.core.calendar.google

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.uuid.UUIDv7
import pro.qyoga.core.users.therapists.TherapistRef
import java.util.*


@Table("therapist_google_accounts")
data class GoogleAccount(
    val ownerRef: TherapistRef,
    val email: String,
    val refreshToken: String,

    @Id val id: UUID = UUIDv7.randomUUID()
)
