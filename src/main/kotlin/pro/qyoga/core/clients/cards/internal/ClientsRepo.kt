package pro.qyoga.core.clients.cards.internal

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.util.TypeInformation
import org.springframework.stereotype.Repository
import pro.qyoga.core.clients.cards.api.Client
import pro.qyoga.core.clients.cards.api.ClientSearchDto
import pro.qyoga.platform.spring.sdj.query.query

@Repository
class ClientsRepo(
    private val jdbcAggregateTemplate: JdbcAggregateOperations,
    jdbcConverter: JdbcConverter
) : SimpleJdbcRepository<Client, Long>(
    jdbcAggregateTemplate,
    BasicPersistentEntity(TypeInformation.of(Client::class.java)),
    jdbcConverter
) {

    fun findBy(therapistId: Long, clientSearchDto: ClientSearchDto, pageRequest: Pageable): Page<Client> {
        val query = query {
            Client::therapistId isEqualIfNotNull therapistId
            Client::firstName isILikeIfNotNull clientSearchDto.firstName
            Client::lastName isILikeIfNotNull clientSearchDto.lastName
            Client::phoneNumber isILikeIfNotNull clientSearchDto.phoneNumber
        }

        return jdbcAggregateTemplate.findAll(query, Client::class.java, pageRequest)
    }

}