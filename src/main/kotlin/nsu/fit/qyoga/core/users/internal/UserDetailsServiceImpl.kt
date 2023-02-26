package nsu.fit.qyoga.core.users.internal

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl: UserDetailsService {

    @Autowired
    private lateinit var usersRepo: UsersRepo

    override fun loadUserByUsername(username: String): UserDetails {
        val user= usersRepo.findByUsername(username)
        if (user == null) {
           // throw { UsernameNotFoundException("User Not Found with username: $username") }
        }
        return UserDetailsImpl.Companion.build(user)
    }
}