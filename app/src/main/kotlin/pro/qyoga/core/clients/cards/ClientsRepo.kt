package pro.qyoga.core.clients.cards

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.spring.sdj.ergo.ErgoRepository
import pro.azhidkov.platform.spring.sdj.query.BuildMode
import pro.azhidkov.platform.spring.sdj.query.query
import pro.azhidkov.platform.spring.sdj.sortBy
import pro.qyoga.core.clients.cards.dtos.ClientSearchDto
import pro.qyoga.core.clients.cards.errors.DuplicatedPhoneException
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.model.PhoneNumber
import pro.qyoga.core.clients.cards.model.toE164Format
import pro.qyoga.core.users.therapists.TherapistRef
import java.util.*

@Repository
class ClientsRepo(
    jdbcAggregateTemplate: JdbcAggregateOperations,
    namedParameterJdbcOperations: NamedParameterJdbcOperations,
    jdbcConverter: JdbcConverter,
    relationalMappingContext: RelationalMappingContext
) : ErgoRepository<Client, UUID>(
    jdbcAggregateTemplate,
    namedParameterJdbcOperations,
    Client::class,
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
    therapistId: UUID,
    clientSearchDto: ClientSearchDto,
    pageRequest: Pageable
): Page<Client> {
    return findPage(pageRequest) {
        Client::therapistRef isEqual therapistId
        Client::firstName isILikeIfNotNull clientSearchDto.firstName
        Client::lastName isILikeIfNotNull clientSearchDto.lastName
        withCriteria(
            Client::phoneNumber isPhoneNumberILikeIfNotNull clientSearchDto.phoneNumber?.replace(
                "[^0-9]".toRegex(),
                ""
            )
        )
    }
}

fun ClientsRepo.findTherapistClientsSliceBySearchKey(
    therapistId: UUID,
    searchKey: String,
    page: Pageable
): Slice<Client> {
    return findSlice(page) {
        Client::therapistRef isEqual therapistId
        and {
            mode = BuildMode.OR
            Client::firstName isILike searchKey
            Client::lastName isILike searchKey
            withCriteria(
                Client::phoneNumber isPhoneNumberILikeIfNotNull searchKey.replace("[^0-9]".toRegex(), "")
                    .takeIf { it.isNotBlank() })
        }
    }
}

fun ClientsRepo.findByPhone(therapistRef: TherapistRef, phone: PhoneNumber): Client? {
    val query = query {
        mode = BuildMode.AND
        Client::therapistRef isEqual therapistRef
        Client::phoneNumber isEqual phone.toE164Format()
    }
    return findOne(query)
}
