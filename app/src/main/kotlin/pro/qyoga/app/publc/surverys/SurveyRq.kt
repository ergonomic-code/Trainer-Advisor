package pro.qyoga.app.publc.surverys

import com.fasterxml.jackson.annotation.JsonProperty
import pro.azhidkov.platform.errors.DomainError
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.model.PhoneNumber
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.l10n.russianDateFormat
import java.time.LocalDate

class InvalidSurveyException(
    msg: String,
    errorCode: String
) : DomainError(msg, errorCode = errorCode) {

    companion object {

        const val MISSING_PHONE = "missing-phone"
        const val MISSING_FIRST_NAME = "missing-first-name"
        const val MISSING_LAST_NAME = "missing-last-name"
        const val SURVEY_SETTINGS_NOT_FOUND_FOR_ADMIN_EMAIL = "survey-settings-not-found-for-admin-email"

        fun missingPhone() = InvalidSurveyException("Survey missing phone", MISSING_PHONE)
        fun missingFirstName() = InvalidSurveyException("Survey missing first name", MISSING_FIRST_NAME)
        fun missingLastName() = InvalidSurveyException("Survey missing last name", MISSING_LAST_NAME)
        fun surveySettingsNotFoundForAdminEmail() =
            InvalidSurveyException("Cannot find survey settings", SURVEY_SETTINGS_NOT_FOUND_FOR_ADMIN_EMAIL)

    }

}

data class SurveyRq(
    val survey: Survey,
    val yandexAdminEmail: String,
    val surveyName: String,
    val questionNames: Map<String, String>
)

data class Survey(
    val id: Long,
    @JsonProperty("survey_id") val surveyId: String,
    val created: String,
    val lang: String,
    val answer: Answer
) {

    val phone = (answer.data[PHONE_NUMBER_FIELD]?.value as? String?
        ?: throw InvalidSurveyException.missingPhone())

    val firstName = answer.data[FIRST_NAME_FIELD]?.value as String?

    val lastName = answer.data[LAST_NAME_FIELD]?.value as String?

    val middleName = answer.data[MIDDLE_NAME_FIELD]?.value as String?

    val birthDate = (answer.data[BIRTH_DATE_FIELD]?.value as String?)
        ?.let { LocalDate.parse(it) }

    val location = (answer.data[LOCATION_FIELD]?.value as? ArrayList<*>)
        ?.firstOrNull()
        ?.let { it as? Map<*, *> }
        ?.get("text") as? String

    val complaints = (answer.data[COMPLAINTS_FIELD]?.value as String?)

    fun nonStandardEntries(): Collection<AnswerDataEntry> = answer.data
        .filterKeys { it !in standardFields }
        .values

    fun toClient(therapistRef: TherapistRef): Client =
        Client(
            firstName = firstName ?: throw InvalidSurveyException.missingFirstName(),
            lastName = lastName ?: throw InvalidSurveyException.missingLastName(),
            middleName = middleName,
            phoneNumber = PhoneNumber.of(phone),
            therapistRef = therapistRef,
            birthDate = birthDate,
            email = null,
            address = location,
            complaints = null,
            anamnesis = null,
            distributionSource = null
        )

    companion object {
        const val PHONE_NUMBER_FIELD = "phoneNumber"
        const val FIRST_NAME_FIELD = "firstName"
        const val LAST_NAME_FIELD = "lastName"
        const val MIDDLE_NAME_FIELD = "middleName"
        const val BIRTH_DATE_FIELD = "birthDate"
        const val LOCATION_FIELD = "location"
        const val COMPLAINTS_FIELD = "complaints"

        val standardFields = setOf(
            PHONE_NUMBER_FIELD,
            FIRST_NAME_FIELD,
            LAST_NAME_FIELD,
            MIDDLE_NAME_FIELD,
            BIRTH_DATE_FIELD,
            LOCATION_FIELD,
            COMPLAINTS_FIELD
        )
    }

}

fun surveyHeader(name: String, date: LocalDate) =
    """=== Анкета "$name" от ${russianDateFormat.format(date)} ==="""

fun formatComplaintsEntry(survey: SurveyRq, date: LocalDate): String? {
    if (survey.survey.complaints == null) {
        return null
    }
    return """${surveyHeader(survey.surveyName, date)}
        |
        |${survey.survey.complaints}
    """.trimMargin()
}

fun formatAnamnesisEntry(surveyRq: SurveyRq, today: LocalDate): String? {
    val questionNamesByAnswers = surveyRq.questionNames.map { it.value to it.key }.toMap()
    val answersText = surveyRq.survey.nonStandardEntries()
        .joinToString("\n\n") { "${questionNamesByAnswers[it.parsedValue()] ?: it.question.slug}: ${it.parsedValue()}" }

    if (answersText.isBlank()) {
        return null
    }

    return """${surveyHeader(surveyRq.surveyName, today)}
             |
             |$answersText
    """.trimMargin()
}


data class Answer(
    val id: Long,
    val data: Map<String, AnswerDataEntry>,
    val survey: SurveyId,
    val created: String
)

data class AnswerDataEntry(
    val value: Any,
    val question: Question
) {

    @Suppress("UNCHECKED_CAST")
    fun parsedValue() = when {
        question.isChoices() ->
            (value as List<Map<String, String>>)
                .map { it["text"] }
                .joinToString()

        else ->
            value
    }

}

data class Question(
    val id: Long,
    val slug: String,
    val options: Map<String, Any>,
    @JsonProperty("answer_type") val answerType: AnswerType
) {

    fun isChoices(): Boolean =
        answerType.slug == "answer_choices"

}

data class AnswerType(
    val id: Int,
    val slug: String
)

data class SurveyId(
    val id: String
)