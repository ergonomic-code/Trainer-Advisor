package nsu.fit.qyoga.core.completingQuestionnaires.internal.repository

import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingClientDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingSearchDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingQuestionnaireDto
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
    fun findQuestionnaireCompletingById(
        therapistId: Long,
        completingSearchDto: CompletingSearchDto,
        pageable: Pageable
    ): Page<CompletingDto> {
        val completingList: MutableList<CompletingDto> = mutableListOf()
        val params = MapSqlParameterSource()
        params.addValue("therapistId", therapistId)
        params.addValue("pageSize", pageable.pageSize)
        params.addValue("offset", pageable.pageSize*pageable.pageNumber)
        params.addValue("name", completingSearchDto.clientName)
        params.addValue("qTitle", completingSearchDto.title)
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
                CompletingQuestionnaireDto(
                    rs.getLong("questionnaireId"),
                    rs.getString("questionnaireTitle")
                ),
                CompletingClientDto(
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
            clients.patronymic as clientPatronymic,
            completing.id AS completingId,
            completing.completing_date AS completingDate,
            completing.numeric_result AS numericResult,
            completing.text_result AS textResult,
            questionnaires.id AS questionnaireId,
            questionnaires.title AS questionnaireTitle
            FROM completing
            LEFT JOIN clients ON clients.id = completing.client_id
            LEFT JOIN questionnaires ON questionnaires.id = completing.questionnaire_id
            WHERE completing.therapist_id = :therapistId
            AND clients.first_name||''||clients.last_name||''||clients.patronymic LIKE '%' || :name || '%'
            AND questionnaires.title LIKE '%' || :qTitle || '%'
            ORDER BY completingDate ${if (type == "UNSORTED") "ASC" else type}
            LIMIT :pageSize OFFSET :offset
        """.trimIndent()
    }
}
