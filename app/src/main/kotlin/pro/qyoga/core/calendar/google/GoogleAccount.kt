package pro.qyoga.core.calendar.google

import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.spring.sdj.converters.SecretChars
import pro.azhidkov.platform.spring.sdj.ergo.hydration.Identifiable
import pro.azhidkov.platform.uuid.UUIDv7
import pro.qyoga.core.users.therapists.TherapistRef
import java.util.*


typealias GoogleAccountRef = AggregateReference<GoogleAccount, UUID>

typealias GoogleAccountId = UUID

@Table("therapist_google_accounts")
data class GoogleAccount(
    val ownerRef: TherapistRef,
    val email: String,
    val refreshToken: SecretChars,

    @Id override val id: GoogleAccountId = UUIDv7.randomUUID()
) : Identifiable<UUID> {

    constructor(
        ownerRef: TherapistRef,
        email: String,
        refreshToken: String
    ) : this(ownerRef, email, SecretChars(refreshToken.toCharArray()))

}