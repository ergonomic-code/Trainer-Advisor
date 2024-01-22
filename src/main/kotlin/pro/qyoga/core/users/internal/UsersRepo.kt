package pro.qyoga.core.users.internal

import org.springframework.dao.DuplicateKeyException
import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.relational.core.conversion.DbActionExecutionException
import org.springframework.data.util.TypeInformation
import org.springframework.stereotype.Repository
import pro.qyoga.core.users.api.User
import pro.qyoga.platform.spring.sdj.erpo.ErgoRepository


@Repository
class UsersRepo(
    override val jdbcAggregateTemplate: JdbcAggregateTemplate,
    jdbcConverter: JdbcConverter
) : ErgoRepository<User, Long>(
    jdbcAggregateTemplate,
    BasicPersistentEntity(TypeInformation.of(User::class.java)),
    jdbcConverter
) {

    fun save(instance: User): User? {
        val insertResult = runCatching {
            super.save(instance)
        }

        val ex = insertResult.exceptionOrNull()
        return when {
            ex == null -> insertResult.getOrThrow()
            ex is DbActionExecutionException && ex.cause is DuplicateKeyException -> null
            else -> throw ex
        }
    }

    fun findByEmail(email: String): User? {
        return findOne {
            User::email isEqual email
        }
    }

}