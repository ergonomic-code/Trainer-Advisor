package pro.qyoga.core.users.internal

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pro.qyoga.core.users.api.User


@Repository
interface UsersMRepo : CrudRepository<User, Long> {

    fun findByEmail(email: String): User?

}