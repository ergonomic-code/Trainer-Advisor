package pro.qyoga.core.clients.cards.internal

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.data.util.TypeInformation
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.qyoga.core.clients.cards.api.Client
import pro.qyoga.core.clients.cards.api.ClientSearchDto
import pro.qyoga.platform.spring.sdj.erpo.ErgoRepository
import pro.qyoga.platform.spring.sdj.query.BuildMode
import pro.qyoga.platform.spring.sdj.sortBy

@Repository
class ClientsRepo(
    jdbcAggregateTemplate: JdbcAggregateOperations,
    namedParameterJdbcOperations: NamedParameterJdbcOperations,
    jdbcConverter: JdbcConverter,
    relationalMappingContext: RelationalMappingContext
) : ErgoRepository<Client, Long>(
    jdbcAggregateTemplate,
    namedParameterJdbcOperations,
    BasicPersistentEntity(TypeInformation.of(Client::class.java)),
    jdbcConverter,
    relationalMappingContext
) {

    object Page {
        val topFiveByLastName = PageRequest.of(0, 5, sortBy(Client::lastName))
    }

}


fun ClientsRepo.findBy(therapistId: Long, clientSearchDto: ClientSearchDto, pageRequest: Pageable): Page<Client> {
    return findAll(pageRequest) {
        Client::therapistId isEqual therapistId
        Client::firstName isILikeIfNotNull clientSearchDto.firstName
        Client::lastName isILikeIfNotNull clientSearchDto.lastName
        Client::phoneNumber isILikeIfNotNull clientSearchDto.phoneNumber
    }
}

fun ClientsRepo.findPageBy(therapistId: Long, searchKey: String, page: Pageable): Iterable<Client> {
    return findSlice(page) {
        Client::therapistId isEqual therapistId
        and {
            mode = BuildMode.OR
            Client::firstName isILike searchKey
            Client::lastName isILike searchKey
            Client::phoneNumber isILike searchKey
        }
    }
}
