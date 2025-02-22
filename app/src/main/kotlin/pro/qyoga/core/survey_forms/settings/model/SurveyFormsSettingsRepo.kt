package pro.qyoga.core.survey_forms.settings.model

import org.intellij.lang.annotations.Language
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.spring.sdj.ergo.ErgoRepository
import pro.qyoga.core.users.therapists.TherapistRef
import java.util.*


@Repository
class SurveyFormsSettingsRepo(
    jdbcAggregateTemplate: JdbcAggregateOperations,
    namedParameterJdbcOperations: NamedParameterJdbcOperations,
    jdbcConverter: JdbcConverter,
    relationalMappingContext: RelationalMappingContext
) : ErgoRepository<SurveyFormsSettings, UUID>(
    jdbcAggregateTemplate,
    namedParameterJdbcOperations,
    SurveyFormsSettings::class,
    jdbcConverter,
    relationalMappingContext
)

fun SurveyFormsSettingsRepo.findByTherapistRef(ref: TherapistRef) =
    findOne {
        SurveyFormsSettings::therapistRef isEqual ref
    }

fun SurveyFormsSettingsRepo.upsertSettings(
    therapistRef: TherapistRef,
    newYandexAdminEmail: String
): UUID {
    @Language("PostgreSQL") val query = """
        INSERT INTO survey_forms_settings (therapist_ref, yandex_admin_email) VALUES (:therapistId, :newYandexAdminEmail)
        ON CONFLICT (therapist_ref) DO UPDATE SET yandex_admin_email = :newYandexAdminEmail
    """.trimIndent()

    return upsert(query, "therapistId" to therapistRef.id, "newYandexAdminEmail" to newYandexAdminEmail)
}

fun SurveyFormsSettingsRepo.findByYandexAdminEmail(email: String) =
    findOne {
        SurveyFormsSettings::yandexAdminEmail isEqual email
    }