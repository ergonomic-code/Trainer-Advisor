package pro.qyoga.tests.cases.app.therapist.survey_forms.settings

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBeComponent
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.object_mothers.survey_forms.SurveyFormsSettingsObjectMother.aSurveyFromsSettingsFrom
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.therapist.survey_forms.SurveyFormsSettingsComponent


@DisplayName("Компонент настроек форм анкет")
class SurveyFormsSettingsComponentTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `должен рендерится корректно`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()

        // Действие
        val document = therapist.settings.getSurveyFormsSettingsComponent()

        // Проверка
        document shouldBeComponent SurveyFormsSettingsComponent
        SurveyFormsSettingsComponent.SurveyFormsSettingsForm.yandexAdminEmail.value(document) shouldBe ""
    }

    @Test
    fun `должен успешно устанавливать изначальное значение email админа в ЯндексФормах`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val updateRequest = aSurveyFromsSettingsFrom()

        // Действие
        val document = therapist.settings.updateSurveyFormsSettings(updateRequest)

        // Проверка
        document shouldBeComponent SurveyFormsSettingsComponent
        SurveyFormsSettingsComponent.SurveyFormsSettingsForm.yandexAdminEmailValue(document) shouldBe updateRequest.yandexAdminEmail

        val requeriedDocument = therapist.settings.getSurveyFormsSettingsComponent()
        requeriedDocument shouldBeComponent SurveyFormsSettingsComponent
        SurveyFormsSettingsComponent.SurveyFormsSettingsForm.yandexAdminEmailValue(requeriedDocument) shouldBe updateRequest.yandexAdminEmail
    }

    @Test
    fun `должен успешно обновлять значение email админа в ЯндексФормах`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        backgrounds.settingsBackgrounds.updateSurveyFormsSettings(THE_THERAPIST_REF, aSurveyFromsSettingsFrom())
        val updateRequest = aSurveyFromsSettingsFrom()

        // Действие
        val document = therapist.settings.updateSurveyFormsSettings(updateRequest)

        // Проверка
        document shouldBeComponent SurveyFormsSettingsComponent
        SurveyFormsSettingsComponent.SurveyFormsSettingsForm.yandexAdminEmailValue(document) shouldBe updateRequest.yandexAdminEmail

        val requeriedDocument = therapist.settings.getSurveyFormsSettingsComponent()
        requeriedDocument shouldBeComponent SurveyFormsSettingsComponent
        SurveyFormsSettingsComponent.SurveyFormsSettingsForm.yandexAdminEmailValue(requeriedDocument) shouldBe updateRequest.yandexAdminEmail
    }

}