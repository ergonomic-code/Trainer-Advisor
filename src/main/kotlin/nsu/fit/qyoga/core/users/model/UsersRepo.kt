package nsu.fit.qyoga.core.users.model

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


@Repository
@Transactional(readOnly = true)
interface UsersRepo : CrudRepository<User, Long> {

    fun findByUsername(username: String): User?

}