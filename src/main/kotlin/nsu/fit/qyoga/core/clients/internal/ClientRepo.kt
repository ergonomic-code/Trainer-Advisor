package nsu.fit.qyoga.core.clients.internal

import nsu.fit.platform.lang.dataClassToMap
import nsu.fit.platform.spring.queryForPage
import nsu.fit.qyoga.core.clients.api.Dto.ClientDto
import nsu.fit.qyoga.core.clients.api.Dto.ClientSearchDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository

@Repository
class ClientRepo(
    private val jdbcTemplate: NamedParameterJdbcOperations
) {

    fun getClientsByFilters(
        search: ClientSearchDto,
        page: Pageable
    ): PageImpl<ClientDto> {
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

        return jdbcTemplate.queryForPage(
            query,
            dataClassToMap(search),
            page,
            DataClassRowMapper(
                ClientDto::class.java
            )
        )

    }

    fun deleteClient(id: Int): Boolean {
        val sql = "DELETE FROM clients WHERE id = :id"
        val paramMap: HashMap<String, Any> = HashMap<String, Any>()
        paramMap["id"] = id
        return jdbcTemplate.update(sql, paramMap) == 1
    }
}
