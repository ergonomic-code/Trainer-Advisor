package pro.qyoga.core.clients.internal

import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.util.TypeInformation
import org.springframework.stereotype.Repository

@Repository
class ClientsRepo(
    jdbcAggregateTemplate: JdbcAggregateOperations,
    jdbcConverter: JdbcConverter
) : SimpleJdbcRepository<Client, Long>(
    jdbcAggregateTemplate,
    BasicPersistentEntity(TypeInformation.of(Client::class.java)),
    jdbcConverter
)
