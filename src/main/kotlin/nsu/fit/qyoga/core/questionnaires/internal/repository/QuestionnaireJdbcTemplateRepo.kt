package nsu.fit.qyoga.core.questionnaires.internal.repository

import nsu.fit.platform.lang.sortDirection
import nsu.fit.platform.spring.queryForPage
import nsu.fit.qyoga.core.questionnaires.api.dtos.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class QuestionnaireJdbcTemplateRepo(
    private val jdbcTemplate: NamedParameterJdbcOperations
) {
    fun findQuestionnaireOnPage(title: String, pageable: Pageable): Page<QuestionnaireDto> {
        val sortDirection = pageable.sortDirection("title") ?: Sort.Direction.ASC
        return jdbcTemplate.queryForPage(
            getQueryBySortType(sortDirection),
            mapOf<String, Any>("title" to title),
            pageable
        ) { rs: ResultSet, _: Int ->
            QuestionnaireDto(
                id = rs.getLong("questionnaireId"),
                title = rs.getString("questionnaireTitle")
            )
        }
    }

    fun getQueryBySortType(sortDirection: Sort.Direction): String {
        return """
            SELECT
            questionnaires.id AS questionnaireId,
            questionnaires.title AS questionnaireTitle
            FROM questionnaires
            WHERE questionnaires.title ILIKE '%' || :title || '%'
            ORDER BY questionnaires.title ${sortDirection.name}
        """.trimIndent()
    }
}
