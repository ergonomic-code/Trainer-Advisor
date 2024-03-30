package pro.qyoga.core.users.auth.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Table
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.Instant

typealias UserRef = AggregateReference<User, Long>

fun UserRef(therapistRef: TherapistRef): UserRef = AggregateReference.to(therapistRef.id!!)

@Table("users")
data class User(
    val email: String,
    val passwordHash: String,
    val roles: Array<Role>,
    val enabled: Boolean,

    @Id
    val id: Long = 0,
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val modifiedAt: Instant? = null,
    @Version
    val version: Long = 0
) {

    init {
        check(roles.size == roles.toSet().size) { "Duplicated roles: $roles" }
    }

    fun disabled() = copy(enabled = false)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (email != other.email) return false
        if (passwordHash != other.passwordHash) return false
        if (!roles.contentEquals(other.roles)) return false
        if (id != other.id) return false
        if (createdAt != other.createdAt) return false
        if (modifiedAt != other.modifiedAt) return false
        if (version != other.version) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + passwordHash.hashCode()
        result = 31 * result + roles.contentHashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + (modifiedAt?.hashCode() ?: 0)
        result = 31 * result + version.hashCode()
        return result
    }

}