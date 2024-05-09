package pro.qyoga.core.users.auth.impl

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import pro.azhidkov.platform.spring.sdj.query.query
import pro.qyoga.core.users.auth.UsersRepo
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.auth.model.User

/**
 * Точка интеграции со Spring Security.
 * Весь остальной код должен уходить в UsersRepo
 */
@Service
class TaUserDetailsService(
    private val usersRepo: UsersRepo
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = usersRepo.findByEmail(username)
            ?: throw BadCredentialsException("Cannot find user by $username")
        return QyogaUserDetails.of(user)
    }

    fun disableUser(login: String) {
        val byEmail = query {
            User::email isEqual login
        }
        usersRepo.updateOne(byEmail) { u -> u.disabled() }
    }

}
