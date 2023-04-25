package nsu.fit.qyoga.core.questionnaires.internal.repository

import nsu.fit.qyoga.core.questionnaires.api.model.Question
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

@Repository
class QuestionJdbcTemplateRepo(
    private val jdbcTemplate: NamedParameterJdbcOperations
) {

    fun updateQuestion(question: Question): Long {
        val query = """
            UPDATE questions 
            SET 
            title = :title,
            question_type = :questionType,
            questionnaire_id = :questionnaireId,
            image_id = :imageId
            WHERE id = :id
        """.trimIndent()
        val params = MapSqlParameterSource()
        params.addValue("title", question.title)
        params.addValue("questionType", question.questionType.name)
        params.addValue("questionnaireId", question.questionnaireId)
        params.addValue("imageId", question.imageId)
        params.addValue("id", question.id)
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(query, params, keyHolder)
        return question.id
    }
}
