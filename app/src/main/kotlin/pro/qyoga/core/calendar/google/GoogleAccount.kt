package pro.qyoga.core.calendar.google

import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.spring.sdj.ergo.hydration.Identifiable
import pro.azhidkov.platform.uuid.UUIDv7
import pro.qyoga.core.users.therapists.TherapistRef
import java.util.*


typealias GoogleAccountRef = AggregateReference<GoogleAccount, UUID>

@Table("therapist_google_accounts")
data class GoogleAccount(
    val ownerRef: TherapistRef,
    val email: String,
    val refreshToken: CharArray,

    @Id override val id: UUID = UUIDv7.randomUUID()
) : Identifiable<UUID> {

    constructor(
        ownerRef: TherapistRef,
        email: String,
        refreshToken: String
    ) : this(ownerRef, email, refreshToken.toCharArray())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GoogleAccount

        if (ownerRef != other.ownerRef) return false
        if (email != other.email) return false
        if (!refreshToken.contentEquals(other.refreshToken)) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ownerRef.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + refreshToken.contentHashCode()
        result = 31 * result + id.hashCode()
        return result
    }

}