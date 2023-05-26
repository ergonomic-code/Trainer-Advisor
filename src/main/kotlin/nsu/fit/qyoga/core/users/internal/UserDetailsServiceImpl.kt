package nsu.fit.qyoga.core.users.internal

import nsu.fit.qyoga.core.users.api.BadCredentials
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val usersRepo: UsersRepo
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = usersRepo.findByUsername(username) ?: throw BadCredentials()
        return QyogaUserDetails.build(user)
    }

}
