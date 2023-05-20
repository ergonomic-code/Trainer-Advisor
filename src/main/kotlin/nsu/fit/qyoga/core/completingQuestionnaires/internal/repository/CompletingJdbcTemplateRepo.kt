package nsu.fit.qyoga.core.completingQuestionnaires.internal.repository

import nsu.fit.qyoga.core.clients.api.Dto.ClientDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingFindDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class CompletingJdbcTemplateRepo(
    private val jdbcTemplate: NamedParameterJdbcOperations
) {
    fun findQuestionnaireCompletingById(questionnaireId: Long, therapistId: Long, completingFindDto: CompletingFindDto, pageable: Pageable): Page<CompletingDto> {
        val completingList: MutableList<CompletingDto> = mutableListOf()
        val params = MapSqlParameterSource()
        params.addValue("questionnaireId", questionnaireId)
        params.addValue("therapistId", therapistId)
        params.addValue("pageSize", pageable.pageSize)
        params.addValue("offset", pageable.pageSize*pageable.pageNumber)
        params.addValue("first", completingFindDto.clientName.substringBefore(" "))
        params.addValue("last", completingFindDto.clientName.substringAfter(" "))
        jdbcTemplate.query(
            getQueryBySortType(pageable.sort.toString().substringAfter(": ")),
            params
        ) { rs: ResultSet, _: Int ->
            val completingId = rs.getLong("completingId")
            if (completingId == 0L) {
                return@query
            }
            val completing = CompletingDto(
                completingId,
                ClientDto(
                    rs.getLong("clientId"),
                    rs.getString("clientFirstName"),
                    rs.getString("clientLastName"),
                    rs.getString("clientPatronymic")
                ),
                rs.getDate("completingDate"),
                rs.getLong("numericResult"),
                rs.getString("textResult")
            )

            completingList.add(completing)
        }
        return PageImpl(completingList, pageable, completingList.size.toLong())
    }

    fun getQueryBySortType(type: String): String {
        return """
            SELECT
            clients.id AS clientId,
            clients.first_name AS clientFirstName,
            clients.last_name AS clientLastName,
            clients.patronymic as clientPatronymic
            completing.id AS completingId,
            completing.completing_date AS completingDate,
            completing.numeric_result AS numericResult,
            completing.text_result AS textResult
            FROM completing
            LEFT JOIN clients ON clients.id = completing.client_id
            WHERE completing.questionnaire_id = :questionnaireId AND completing.therapist_id = :therapistId
            AND ( clients.first_name LIKE '%' || :first || '%' OR clients.first_name LIKE '%' || :last || '%' )
            AND ( clients.last_name LIKE '%' || :first || '%' OR clients.last_name LIKE '%' || :last || '%' )
            ORDER BY completingDate $type
            LIMIT :pageSize OFFSET :offset
        """.trimIndent()
    }

}