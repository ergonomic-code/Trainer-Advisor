package pro.qyoga.core.survey_forms.settings.model

import org.springframework.data.annotation.*
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.spring.sdj.ergo.hydration.Identifiable
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.Instant
import java.util.*

@Table("survey_forms_settings")
data class SurveyFormsSettings(
    @Id val therapistRef: TherapistRef,
    val yandexAdminEmail: String?,

    @CreatedDate val createdAt: Instant = Instant.now(),
    @LastModifiedDate val lastModifiedAt: Instant? = null,
    @Version val version: Long = 0,
) : Identifiable<UUID> {

    @Transient
    override val id: UUID = therapistRef.id!!

    companion object {

        fun createDefaultSettings(therapistRef: TherapistRef) =
            SurveyFormsSettings(
                therapistRef = therapistRef,
                yandexAdminEmail = ""
            )

    }

}