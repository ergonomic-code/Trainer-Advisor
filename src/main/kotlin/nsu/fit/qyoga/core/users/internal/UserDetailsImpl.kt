package nsu.fit.qyoga.core.users.internal

import nsu.fit.qyoga.core.users.api.Role
import nsu.fit.qyoga.core.users.api.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

class UserDetailsImpl(
    private val username: String,
    private val password: String,
    private val id: Long,
    private val roles: Collection<GrantedAuthority>
) : UserDetails {
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return this.roles
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return this.username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    fun getId(): Long {
        return id
    }

    companion object {
        fun build(user: User): UserDetailsImpl {
            val authorities: List<GrantedAuthority> = user.roles.stream()
                .map { role: Role ->
                    SimpleGrantedAuthority(role.toString())
                }
                .collect(Collectors.toList())
            return UserDetailsImpl(
                user.username,
                user.passwordHash,
                user.id,
                authorities
            )
        }
    }
}
