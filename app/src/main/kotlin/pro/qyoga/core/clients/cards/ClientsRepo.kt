package pro.qyoga.core.clients.cards

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.data.util.TypeInformation
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.spring.sdj.erpo.ErgoRepository
import pro.azhidkov.platform.spring.sdj.query.BuildMode
import pro.azhidkov.platform.spring.sdj.sortBy
import pro.qyoga.core.clients.cards.dtos.ClientSearchDto
import pro.qyoga.core.clients.cards.errors.DuplicatedPhoneException
import pro.qyoga.core.clients.cards.model.Client

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

    override fun <S : Client?> save(instance: S & Any): S & Any {
        return saveAndMapDuplicatedKeyException(instance) { ex ->
            DuplicatedPhoneException(instance, ex)
        }
    }

}


fun ClientsRepo.findTherapistClientsPageBySearchForm(
    therapistId: Long,
    clientSearchDto: ClientSearchDto,
    pageRequest: Pageable
): Page<Client> {
    return findPage(pageRequest) {
        Client::therapistId isEqual therapistId
        Client::firstName isILikeIfNotNull clientSearchDto.firstName
        Client::lastName isILikeIfNotNull clientSearchDto.lastName
        Client::phoneNumber isILikeIfNotNull clientSearchDto.phoneNumber
    }
}

fun ClientsRepo.findTherapistClientsSliceBySearchKey(
    therapistId: Long,
    searchKey: String,
    page: Pageable
): Slice<Client> {
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
