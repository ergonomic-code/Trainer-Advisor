package pro.qyoga.core.users.internal

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import pro.qyoga.core.users.api.Role
import pro.qyoga.core.users.api.User
import java.util.stream.Collectors

data class QyogaUserDetails(
    val id: Long,
    private val username: String,
    private val password: String,
    private val roles: Collection<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = this.roles

    override fun getPassword(): String = this.password

    override fun getUsername(): String = this.username

    override fun isAccountNonLocked() = true

    override fun isAccountNonExpired(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    companion object {

        fun of(user: User): QyogaUserDetails {
            val authorities: List<GrantedAuthority> = user.roles.stream()
                .map { role: Role ->
                    SimpleGrantedAuthority(role.toString())
                }
                .collect(Collectors.toList())
            return QyogaUserDetails(
                user.id,
                user.email,
                user.passwordHash,
                authorities
            )
        }

    }

}
