package pro.qyoga.core.clients.cards

import org.intellij.lang.annotations.Language
import org.springframework.data.domain.*
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

    companion object {
        val descendingTouchTime = Sort.by(Sort.Direction.DESC, "touch_time")
    }

}

fun ClientsRepo.findTherapistClientsPageBySearchForm(
    therapistId: UUID,
    clientSearchDto: ClientSearchDto,
    pageRequest: Pageable
): Page<Client> {
    @Language("PostgreSQL") val query = """
        SELECT c.*,
             GREATEST(c.created_at, c.modified_at, le.created_at, le.last_modified_at) touch_time
        FROM clients c
            LEFT JOIN client_last_journal_entries le ON le.client_ref = c.id
        WHERE c.therapist_ref = :therapistRef
            AND c.first_name ILIKE '%' || :firstName || '%'
            AND c.last_name ILIKE '%' || :lastName || '%'
            AND c.phone_number ILIKE '%' || :phoneNumber || '%'
    """

    val paramMap = mapOf(
        "therapistRef" to therapistId,
        "firstName" to (clientSearchDto.firstName ?: ""),
        "lastName" to (clientSearchDto.lastName ?: ""),
        "phoneNumber" to (clientSearchDto.digitsOnlyPhoneNumber ?: "")
    )
    return findPage(query, paramMap, pageRequest)
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
