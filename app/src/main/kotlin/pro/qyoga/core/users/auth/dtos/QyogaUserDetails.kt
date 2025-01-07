package pro.qyoga.core.users.auth.dtos

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import pro.qyoga.core.users.auth.model.User
import java.util.*

data class QyogaUserDetails(
    val id: UUID,
    private val username: String,
    private val password: String,
    private val roles: Collection<GrantedAuthority>,
    private val enabled: Boolean
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = this.roles

    override fun getPassword(): String = this.password

    override fun getUsername(): String = this.username

    override fun isAccountNonLocked() = true

    override fun isAccountNonExpired(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = enabled

    companion object {

        fun of(user: User): QyogaUserDetails {
            val authorities = user.roles
                .map { SimpleGrantedAuthority(it.toString()) }
            return QyogaUserDetails(
                user.id,
                user.email,
                user.passwordHash,
                authorities,
                user.enabled
            )
        }

    }

}

