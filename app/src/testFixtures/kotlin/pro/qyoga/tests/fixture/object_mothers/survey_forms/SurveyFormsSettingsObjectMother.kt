package pro.qyoga.tests.fixture.object_mothers.survey_forms

import pro.qyoga.app.therapist.survey_forms.settings.SurveyFormsSettingsForm
import pro.qyoga.tests.fixture.data.faker


object SurveyFormsSettingsObjectMother {

    fun aSurveyFromsSettingsFrom(
        yandexAdminEmail: String = faker.internet().emailAddress()
    ) = SurveyFormsSettingsForm(yandexAdminEmail)

}