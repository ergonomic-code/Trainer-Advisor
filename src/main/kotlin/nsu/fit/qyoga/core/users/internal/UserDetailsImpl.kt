package nsu.fit.qyoga.core.users.internal

import nsu.fit.qyoga.core.users.api.BadCredentials
import nsu.fit.qyoga.core.users.api.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors


class UserDetailsImpl(private val username: String?,
                      private val password: String?, private val roles: Collection<out GrantedAuthority>?, private val id: Long?
): UserDetails{

    /*constructor(
        id: Long, username: String,  password: String,
        authorities: MutableCollection<out GrantedAuthority>
    ) : this(id, username, password, authorities) */
    companion object {
        fun build(user: User?): UserDetailsImpl {
            val authorities: List<GrantedAuthority> = user?.roles?.stream()
                ?.map { role: Role ->
                    SimpleGrantedAuthority(role.toString())
                }
                ?.collect(Collectors.toList())!!
            if (user != null) {
                return UserDetailsImpl(
                    user?.username,
                    user?.passwordHash,
                    authorities,
                    user?.id
                )
            }
            throw BadCredentials()
        }
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun getAuthorities(): Collection<out GrantedAuthority>? {
        return this.roles
    }

    override fun getPassword(): String? {
        return this.password
    }

    override fun getUsername(): String? {
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
    /*@Transient
    private val roles: List<*> = listOf("roles")
    fun getRoles(): List<*>{
        return roles
    }*/
}

/*private fun <T> Stream<T>?.Collect(toList: Any): List<T> {

}
*/