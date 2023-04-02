package nsu.fit.qyoga.core.questionnaires.internal.repository

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository

@Repository
class AnswerJdbcTemplateRepo(
    private val jdbcTemplate: NamedParameterJdbcOperations
) {

    fun updateAnswerTitleById(id: Long, title: String){
        val query = """
            UPDATE answers
            SET title = :title
            WHERE id = :id
        """.trimIndent()
        val param = MapSqlParameterSource()
        param.addValue("id", id)
        param.addValue("title", title)
        jdbcTemplate.update(query, param)
    }

    fun deleteAnswerByQuestionId(id: Long){
        val query = """
            DELETE FROM answers
            WHERE question_id = :id
        """.trimIndent()
        jdbcTemplate.update(query, MapSqlParameterSource("id", id))
    }
}