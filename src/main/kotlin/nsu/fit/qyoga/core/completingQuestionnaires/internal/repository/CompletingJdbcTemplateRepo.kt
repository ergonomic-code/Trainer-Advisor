package nsu.fit.qyoga.core.completingQuestionnaires.internal.repository

import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.ClientDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingListDto
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class CompletingJdbcTemplateRepo(
    private val jdbcTemplate: NamedParameterJdbcOperations
) {
    private final val getQuestionnaireTitleQuery = """
        SELECT title AS title
        FROM questionnaires
        WHERE questionnaires.id = :questionnaireId
    """.trimIndent()

    private final val getCompletingByQId = """
        SELECT
        clients.id AS clientId,
        clients.first_name AS clientFirstName,
        clients.last_name AS clientLastName,
        completing.id AS completingId,
        completing.completing_date AS completingDate,
        completing.numeric_result AS numericResult,
        completing.text_result AS textResult
        FROM completing
        LEFT JOIN clients ON clients.id = completing.client_id
        WHERE completing.questionnaire_id = :questionnaireId
        ORDER BY completingDate DESC
    """.trimIndent()

    fun findQuestionnaireCompletingById(questionnaireId: Long): CompletingListDto? {
        val completingListDto = jdbcTemplate.queryForObject<CompletingListDto>(
            getQuestionnaireTitleQuery,
            MapSqlParameterSource("questionnaireId", questionnaireId)
        ) { rs: ResultSet, _: Int ->
            CompletingListDto(questionnaireId, rs.getString("title"))
        }
        completingListDto ?: return null
        jdbcTemplate.query(
            getCompletingByQId,
            MapSqlParameterSource("questionnaireId", questionnaireId)
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
                    rs.getString("clientLastName")
                ),
                rs.getDate("completingDate"),
                rs.getLong("numericResult"),
                rs.getString("textResult")
            )

            completingListDto.resultList += completing
        }
        return completingListDto
    }
}