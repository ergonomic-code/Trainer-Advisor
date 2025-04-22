package pro.qyoga.app.therapist.clients.list

import org.intellij.lang.annotations.Language
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.support.DefaultConversionService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.jdbc.core.DataClassRowMapper
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.cards.dtos.ClientSearchDto
import pro.qyoga.core.users.therapists.Therapist
import java.util.*

fun ClientsRepo.findTherapistClientsPageBySearchForm(
    therapistId: UUID,
    clientSearchDto: ClientSearchDto,
    pageRequest: Pageable
): Page<ClientListItemView> {
    @Language("PostgreSQL") val query = """
        SELECT c.*,
             le.date last_journal_entry_date,
             GREATEST(c.created_at, c.modified_at, le.created_at, le.last_modified_at) touch_time,
             (SELECT COUNT(*) FROM journal_entries je WHERE je.client_ref = c.id) journal_entries_count
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
    val clientListItemViewRowMapper = DataClassRowMapper(ClientListItemView::class.java).apply {
        conversionService = DefaultConversionService().apply {
            @Suppress("ObjectLiteralToLambda") val converter =
                object : Converter<UUID, AggregateReference<Therapist, UUID>> {
                    override fun convert(source: UUID): AggregateReference<Therapist, UUID>? {
                        return AggregateReference.to(source)
                    }
                }
            addConverter(converter)
        }
    }
    return findPage(query, paramMap, pageRequest, clientListItemViewRowMapper)
}