package nsu.fit.qyoga.core.clients.internal

import nsu.fit.platform.spring.queryForPage
import nsu.fit.qyoga.core.clients.api.Dto.ClientListDto
import nsu.fit.qyoga.core.clients.api.Dto.ClientListSearchDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository

@Repository
class ClientRepo(
    private val jdbcTemplate: NamedParameterJdbcOperations
) {

    fun getClientsByFilters(
        search: ClientListSearchDto,
        page: Pageable
    ): PageImpl<ClientListDto> {
        val query = """
            SELECT clients.id,
                   clients.first_name,
                   clients.last_name,
                   clients.patronymic,
                   clients.birth_date,
                   clients.phone_number,
                   clients.diagnose,
                   clients.email,
                   clients.distribution_source
            FROM appointments
                     INNER JOIN clients ON clients.id = appointments.client_id
            WHERE
                (:firstName::varchar IS NULL OR first_name ilike '%' || :firstName || '%') AND
                (:lastName::varchar IS NULL OR last_name ilike  '%' || :lastName || '%') AND
                (:patronymic::varchar IS NULL OR patronymic ilike  '%' || :patronymic || '%') AND
                (:phoneNumber::varchar IS NULL OR phone_number ilike '%' || :phoneNumber || '%') AND
                (:diagnose::varchar IS NULL OR diagnose ilike    '%' || :diagnose || '%') 
            GROUP BY clients.id
            HAVING (:appointmentDate::varchar IS NULL OR :appointmentDate =ANY (ARRAY_AGG(DATE_TRUNC('day', appointments.datetime))))
        """.trimIndent()

        val filterParams = mapOf(
            "firstName" to search.firstName,
            "lastName" to search.lastName,
            "patronymic" to search.patronymic,
            "phoneNumber" to search.phoneNumber,
            "diagnose" to search.diagnose,
            "appointmentDate" to search.appointmentDate,
        )

        return jdbcTemplate.queryForPage(query, filterParams, page) { rs, _ ->
            ClientListDto(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("patronymic")
            )
        }
    }

}
