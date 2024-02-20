package pro.qyoga.core.users.auth

import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.data.util.TypeInformation
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.spring.sdj.erpo.ErgoRepository
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
    BasicPersistentEntity(TypeInformation.of(User::class.java)),
    jdbcConverter,
    relationalMappingContext
) {

    fun findByEmail(email: String): User? {
        return findOne {
            User::email isEqual email
        }
    }

}