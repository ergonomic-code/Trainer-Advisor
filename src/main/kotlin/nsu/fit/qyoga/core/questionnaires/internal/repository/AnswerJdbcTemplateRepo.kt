package nsu.fit.qyoga.core.questionnaires.internal.repository

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

@Repository
class AnswerJdbcTemplateRepo(
    private val jdbcTemplate: NamedParameterJdbcOperations
) {
    fun createAnswerByQuestionId(id: Long): Long {
        val query = """
            INSERT INTO answers 
            (title, lower_bound, lower_bound_text, upper_bound, upper_bound_text, score, question_id, image_id)
            VALUES
            ('', null, null, null, null, null, :questionId, null)
            RETURNING id
        """.trimIndent()
        val params = MapSqlParameterSource()
        params.addValue("questionId", id)
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(query, params, keyHolder)
        return keyHolder.key!!.toLong()
    }
}
