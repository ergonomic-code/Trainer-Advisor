package pro.qyoga.tests.fixture.presets

import org.springframework.stereotype.Component
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.tests.fixture.backgrounds.ClientsBackgrounds
import pro.qyoga.tests.fixture.backgrounds.TherapistSettingsBackgrounds
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.survey_forms.SurveyFormsSettingsObjectMother.aSurveyFromsSettingsFrom
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF

data class TherapistSettingsFixture(
    val yandexAdminEmail: String,
)

data class ClientFixture(
    val client: Client
) {

    val clientPhone = client.phoneNumber

    val complaints: String? = client.complaints

    val anamnesis: String? = client.anamnesis

}

data class ExistingClientPreset(
    val yandexAdminEmail: TherapistSettingsFixture,
    val clientPhone: ClientFixture
)

@Component
class SurveysFixturePresets(
    private val therapistSettingsBackgrounds: TherapistSettingsBackgrounds,
    private val clientsBackgrounds: ClientsBackgrounds
) {

    fun createSingleClient(
        clientDto: ClientCardDto = ClientsObjectMother.createClientCardDtoMinimal(),
        yandexAdminEmail: String = faker.internet().emailAddress()
    ): ExistingClientPreset {
        therapistSettingsBackgrounds.createSurveyFormsSettings(
            THE_THERAPIST_REF,
            aSurveyFromsSettingsFrom(yandexAdminEmail)
        )
        val client = clientsBackgrounds.createClient(clientDto)
        return ExistingClientPreset(TherapistSettingsFixture(yandexAdminEmail), ClientFixture(client))
    }

}