package pro.qyoga.core.users.auth.impl

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import pro.qyoga.core.users.auth.UsersRepo
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails

/**
 * Точка интеграции со Spring Security.
 * Весь остальной код должен уходить в UsersRepo
 */
@Service
class UserDetailsServiceImpl(
    private val usersRepo: UsersRepo
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = usersRepo.findByEmail(username)
            ?: throw BadCredentialsException("Cannot find user by $username")
        return QyogaUserDetails.of(user)
    }

}
