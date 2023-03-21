package nsu.fit.qyoga.core.questionnaires.internal.repository

import nsu.fit.qyoga.core.questionnaires.api.dtos.*
import nsu.fit.qyoga.core.questionnaires.api.enums.QuestionType
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class QuestionnaireJdbcTemplateRepo(
    private val jdbcTemplate: NamedParameterJdbcOperations
) {

    /*
    * вспомогательный метод для получения ответов
    */
    private fun getQuestion(
        questionMap: MutableMap<Long, QuestionWithAnswersDto?>,
        id: Long,
        questionFromDB: QuestionWithAnswersDto,
        questionnaire: QuestionnaireWithQuestionDto
    ): QuestionWithAnswersDto {
        var question: QuestionWithAnswersDto? = questionMap[id]
        if (question == null) {
            questionMap[id] = questionFromDB
            question = questionFromDB
            questionnaire.questions.add(questionFromDB)
        }
        return question
    }

    fun findQuestionnaireWithQuestionsById(id: Long): QuestionnaireWithQuestionDto? {
        val query = """
        SELECT 
           questionnaires.title AS questionnaireTitle,
           questions.id AS questionId,
           questions.title AS questionTitle,
           questions.question_type AS questionType,
           questionImage.data AS questionImage,
           answers.title AS answerTitle,
           answers.lower_bound AS answerLowerBound,
           answers.lower_bound_text AS answerLowerBoundText,
           answers.upper_bound AS answerUpperBound,
           answers.upper_bound_text AS answerUpperBoundText,
           answers.score AS answerScore,
           answerImage.data AS answerImage
        FROM questionnaires
            LEFT JOIN questions ON questionnaires.id = questions.questionnaire_id
            LEFT JOIN images questionImage ON questions.image_id = questionImage.id
            LEFT JOIN answers ON answers.question_id = questions.id
            LEFT JOIN images answerImage ON answers.image_id = answerImage.id
        WHERE questionnaires.id = :id
        """.trimIndent()
        val value = QuestionnaireWithQuestionDto(title = null)
        val questionMap: MutableMap<Long, QuestionWithAnswersDto?> = mutableMapOf()
        jdbcTemplate.query(
            query,
            MapSqlParameterSource("id", id),
        ){ rs: ResultSet, _: Int ->
            if (value.title  == null) {
                value.title = rs.getString("questionnaireTitle")
            }
            val questionId = rs.getLong("questionId")
            val questionFromDB = QuestionWithAnswersDto(
                rs.getString("questionTitle"),
                QuestionType.valueOf(rs.getString("questionType") ?: return@query),
                null
            )
            val answer = AnswerDto(
                rs.getString("answerTitle"),
                rs.getInt("answerLowerBound"),
                rs.getString("answerLowerBoundText"),
                rs.getInt("answerUpperBound"),
                rs.getString("answerUpperBoundText"),
                rs.getInt("answerScore"),
                null
            )
            val question = getQuestion(questionMap, questionId, questionFromDB, value)
            if(!(answer.title == null && answer.lowerBound == 0 && answer.upperBound == 0)) {
                question.answers.add(answer)
            }
        }
        return value
    }
}