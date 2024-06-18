package pro.qyoga.core.users.auth

import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import pro.azhidkov.platform.spring.sdj.ergo.ErgoRepository
import pro.qyoga.core.users.auth.errors.DuplicatedEmailException
import pro.qyoga.core.users.auth.model.User


@Repository
class UsersRepo(
    jdbcAggregateTemplate: JdbcAggregateTemplate,
    namedParameterJdbcOperations: NamedParameterJdbcOperations,
    jdbcConverter: JdbcConverter,
    relationalMappingContext: RelationalMappingContext
) : ErgoRepository<User, Long>(
    jdbcAggregateTemplate,
    namedParameterJdbcOperations,
    User::class,
    jdbcConverter,
    relationalMappingContext
) {

    @Transactional
    override fun <S : User?> save(instance: S & Any): S & Any {
        return saveAndMapDuplicatedKeyException(instance) { ex ->
            DuplicatedEmailException(instance, ex)
        }
    }

    fun findByEmail(email: String): User? {
        return findOne {
            User::email isEqual email
        }
    }

}