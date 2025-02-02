package pro.qyoga.app.publc.surverys

import com.fasterxml.jackson.annotation.JsonProperty
import pro.azhidkov.platform.errors.DomainError


data class SurveyRq(
    val id: Long,
    @JsonProperty("survey_id") val surveyId: String,
    val created: String,
    val lang: String,
    val answer: Answer
) {

    val phone = (answer.data["phone"]?.value as? String?)
        ?: throw InvalidSurveyException(this, "Survey missing string phone")

}

class InvalidSurveyException(
    val surveyRq: SurveyRq,
    val msg: String
) : DomainError(msg)

data class Answer(
    val id: Long,
    val data: Map<String, AnswerDataEntry>,
    val survey: Survey,
    val created: String
)

data class AnswerDataEntry(
    val value: Any,
    val question: Question
)

data class Question(
    val id: Long,
    val slug: String,
    val options: Map<String, Any>,
    @JsonProperty("answer_type") val answerType: AnswerType
)

data class AnswerType(
    val id: Int,
    val slug: String
)

data class Survey(
    val id: String
)