package nsu.fit.qyoga.core.questionnaires.internal.repository

import nsu.fit.qyoga.core.questionnaires.api.dtos.AnswerDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionWithAnswersDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.enums.QuestionType
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class QuestionJdbcTemplateRepo(
    private val jdbcTemplate: NamedParameterJdbcOperations
) {
    fun findQuestionWithAnswersById(id: Long): QuestionWithAnswersDto? {
        val query = """
        SELECT 
           questions.id AS questionId,
           questions.title AS questionTitle,
           questions.question_type AS questionType,
           questions.questionnaire_id AS questionnaireId,
           questionImage.id AS questionImageId,
           answers.id AS answerId,
           answers.title AS answerTitle,
           answers.lower_bound AS answerLowerBound,
           answers.lower_bound_text AS answerLowerBoundText,
           answers.upper_bound AS answerUpperBound,
           answers.upper_bound_text AS answerUpperBoundText,
           answers.score AS answerScore,
           answerImage.id AS answerImageId
        FROM questions
            LEFT JOIN images questionImage ON questions.image_id = questionImage.id
            LEFT JOIN answers ON answers.question_id = questions.id
            LEFT JOIN images answerImage ON answers.image_id = answerImage.id
        WHERE questions.id = :id
        """.trimIndent()
        var value: QuestionWithAnswersDto? = null
        jdbcTemplate.query(
            query,
            MapSqlParameterSource("id", id)
        ) { rs: ResultSet, _: Int ->
            if (value == null) {
                value = QuestionWithAnswersDto(
                    id = rs.getLong("questionId"),
                    title = rs.getString("questionTitle"),
                    questionType = QuestionType.valueOf(rs.getString("questionType") ?: return@query),
                    imageId = rs.getString("questionImageId")?.toLong(),
                    questionnaireId = rs.getLong("questionnaireId"),
                    answers = mutableListOf()
                )
            }
            val answer = AnswerDto(
                id = rs.getLong("answerId"),
                title = rs.getString("answerTitle"),
                lowerBound = rs.getInt("answerLowerBound"),
                lowerBoundText = rs.getString("answerLowerBoundText"),
                upperBound = rs.getInt("answerUpperBound"),
                upperBoundText = rs.getString("answerUpperBoundText"),
                score = rs.getInt("answerScore"),
                imageId = rs.getString("answerImageId")?.toLong(),
                questionId = rs.getLong("questionId")
            )
            value!!.answers += answer
        }
        return value
    }
}
