package nsu.fit.qyoga.core.users.internal

import nsu.fit.qyoga.core.users.api.Role
import nsu.fit.qyoga.core.users.api.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors


data class UserPrincipal(private val user: User) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return user.roles.stream()
            .map { role: Role ->
                SimpleGrantedAuthority(role.toString())
            }
            .collect(Collectors.toList())
    }

    fun getId(): Long {
        return user.id
    }

    override fun getPassword(): String {
        return user.passwordHash
    }

    override fun getUsername(): String {
        return user.username
    }

    override fun isAccountNonExpired(): Boolean {
        return false
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}