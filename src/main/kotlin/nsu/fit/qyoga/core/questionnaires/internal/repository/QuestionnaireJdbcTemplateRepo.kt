package nsu.fit.qyoga.core.questionnaires.internal.repository

import nsu.fit.qyoga.core.questionnaires.api.dtos.*
import nsu.fit.qyoga.core.questionnaires.api.dtos.enums.QuestionType
import nsu.fit.qyoga.core.questionnaires.api.model.Questionnaire
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class QuestionnaireJdbcTemplateRepo(
    private val jdbcTemplate: NamedParameterJdbcOperations
) {
    fun findQuestionnaireOnPage(title: String, pageable: Pageable): Page<QuestionnaireDto> {
        val questionnaireDtoList: MutableList<QuestionnaireDto> = mutableListOf()
        val params = MapSqlParameterSource()
        params.addValue("pageSize", pageable.pageSize)
        params.addValue("title", title)
        params.addValue("offset", pageable.pageSize*pageable.pageNumber)
        jdbcTemplate.query(
            getQueryBySortType(pageable.sort.toString().substringAfter(": ")),
            params
        ) { rs: ResultSet, _: Int ->
            val questionnaireId = rs.getLong("questionnaireId")
            if (questionnaireId == 0L) {
                return@query
            }
            questionnaireDtoList.add(
                QuestionnaireDto(
                    id = questionnaireId,
                    title = rs.getString("questionnaireTitle")
                )
            )
        }
        return PageImpl(questionnaireDtoList, pageable, getCount(title))
    }

    fun getCount(title: String): Long {
        val query = """
            SELECT
            count(*) as questionnaireCount
            FROM questionnaires
            WHERE questionnaires.title LIKE '%' || :title || '%'
        """.trimIndent()
        return jdbcTemplate.queryForObject(
            query,
            MapSqlParameterSource("title", title)
        ) {rs: ResultSet, _: Int ->
            rs.getLong("questionnaireCount")
        } ?: 0
    }


    fun getQueryBySortType(type: String): String {
        return """
            SELECT
            questionnaires.id AS questionnaireId,
            questionnaires.title AS questionnaireTitle
            FROM questionnaires
            WHERE questionnaires.title LIKE '%' || :title || '%'
            ORDER BY questionnaires.title ${if (type == "UNSORTED") "ASC" else type}
            LIMIT :pageSize OFFSET :offset
        """.trimIndent()
    }
}
