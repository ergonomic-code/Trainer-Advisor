package nsu.fit.qyoga.core.questionnaires.internal.repository

import nsu.fit.qyoga.core.questionnaires.api.dtos.AnswerBoundsDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.AnswerDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionWithAnswersDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireWithQuestionDto
import nsu.fit.qyoga.core.questionnaires.api.dtos.enums.QuestionType
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class QuestionnaireJdbcTemplateRepo(
    private val jdbcTemplate: NamedParameterJdbcOperations
) {
    val getQreWithQByIdQuery = """
        SELECT
        questionnaires.id AS questionnaireId,
        questionnaires.title AS questionnaireTitle,
        questions.id AS questionId,
        questions.title AS questionTitle,
        questions.question_type AS questionType,
        questionImage.id AS questionImageId,
        answers.id AS answerId,
        answers.title AS answerTitle,
        answers.lower_bound AS answerLowerBound,
        answers.lower_bound_text AS answerLowerBoundText,
        answers.upper_bound AS answerUpperBound,
        answers.upper_bound_text AS answerUpperBoundText,
        answers.score AS answerScore,
        answerImage.id AS answerImageId
        FROM questionnaires
        LEFT JOIN questions ON questionnaires.id = questions.questionnaire_id
        LEFT JOIN images questionImage ON questions.image_id = questionImage.id
        LEFT JOIN answers ON answers.question_id = questions.id
        LEFT JOIN images answerImage ON answers.image_id = answerImage.id
        WHERE questionnaires.id = :id
        ORDER BY questionId, answerId
    """.trimIndent()

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
            questionnaire.questions += questionFromDB
        }
        return question
    }

    fun getQreWithQById(id: Long): QuestionnaireWithQuestionDto? {
        var value: QuestionnaireWithQuestionDto? = null
        val questionMap: MutableMap<Long, QuestionWithAnswersDto?> = mutableMapOf()
        jdbcTemplate.query(
            getQreWithQByIdQuery,
            MapSqlParameterSource("id", id)
        ) { rs: ResultSet, _: Int ->
            if (value == null) {
                value = QuestionnaireWithQuestionDto(
                    id = rs.getLong("questionnaireId"),
                    title = rs.getString("questionnaireTitle"),
                    questions = mutableListOf()
                )
            }
            val questionFromDB = getQuestionWithAnswersDto(rs)
            questionFromDB ?: return@query
            val questionId = rs.getLong("questionId")
            val answer = getAnswerDto(rs)
            val question = getQuestion(questionMap, questionId, questionFromDB, value!!)
            if (rs.getString("answerId") != null) {
                question.answers += answer
            }
        }
        return value
    }

    fun getQuestionWithAnswersDto(rs: ResultSet): QuestionWithAnswersDto? {
        return QuestionWithAnswersDto(
            id = rs.getLong("questionId"),
            title = rs.getString("questionTitle"),
            questionType = QuestionType.valueOf(rs.getString("questionType") ?: return null),
            imageId = rs.getString("questionImageId")?.toLong(),
            answers = mutableListOf(),
            questionnaireId = rs.getLong("questionnaireId")
        )
    }

    fun getAnswerDto(rs: ResultSet): AnswerDto {
        return AnswerDto(
            id = rs.getLong("answerId"),
            title = rs.getString("answerTitle") ?: "",
            bounds = AnswerBoundsDto(
                lowerBound = rs.getInt("answerLowerBound"),
                lowerBoundText = rs.getString("answerLowerBoundText"),
                upperBound = rs.getInt("answerUpperBound"),
                upperBoundText = rs.getString("answerUpperBoundText")
            ),
            score = rs.getInt("answerScore"),
            imageId = rs.getString("answerImageId")?.toLong()
        )
    }
}