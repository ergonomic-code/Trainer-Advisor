package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import pro.qyoga.app.therapist.survey_forms.settings.SurveyFormsSettingsForm
import pro.qyoga.core.survey_forms.settings.model.SurveyFormsSettingsRepo
import pro.qyoga.core.survey_forms.settings.model.upsertSettings
import pro.qyoga.core.users.therapists.TherapistRef


@Component
class TherapistSettingsBackgrounds(
    private val surveyFormsSettingsRepo: SurveyFormsSettingsRepo
) {

    fun createSurveyFormsSettings(therapist: TherapistRef, createRequest: SurveyFormsSettingsForm) {
        surveyFormsSettingsRepo.upsertSettings(therapist, createRequest.yandexAdminEmail)
    }

    fun updateSurveyFormsSettings(therapist: TherapistRef, updateRequest: SurveyFormsSettingsForm) {
        surveyFormsSettingsRepo.upsertSettings(therapist, updateRequest.yandexAdminEmail)
    }

}