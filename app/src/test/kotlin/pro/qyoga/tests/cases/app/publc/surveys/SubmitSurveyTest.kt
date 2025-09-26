package pro.qyoga.tests.cases.app.publc.surveys

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.annotation.DisplayName
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.springframework.http.HttpStatus
import pro.qyoga.app.publc.surverys.InvalidSurveyException
import pro.qyoga.app.publc.surverys.Survey
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.model.PhoneNumber
import pro.qyoga.core.clients.cards.model.toUIFormat
import pro.qyoga.tests.cases.app.publc.surveys.request_bodies.*
import pro.qyoga.tests.clients.YandexFormsClient
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.survey_forms.SurveyFormsSettingsObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_ADMIN_LOGIN
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseKoTest
import pro.qyoga.tests.infra.web.mainWebTestClient


@DisplayName("Операция создания анкеты")
class SubmitSurveyTest : QYogaAppIntegrationBaseKoTest({

    val yandexFormsClient by lazy { YandexFormsClient(mainWebTestClient) }

    "при отправке новым клиентом корректного запроса со всеми значениями карточки должна" - {
        // Сетап
        val entranceSurveyJson = jacksonObjectMapper().readTree(entranceSurveyJsonStr)

        val yandexAdminEmail = entranceSurveyJson["yandexAdminEmail"].textValue()
        backgrounds.settingsBackgrounds.createSurveyFormsSettings(
            THE_THERAPIST_REF,
            SurveyFormsSettingsObjectMother.aSurveyFromsSettingsFrom(yandexAdminEmail)
        )

        // Действие
        val status = yandexFormsClient.createSurvey(entranceSurveyJsonStr)

        // Проверка
        "возвращать статус 204" {
            status shouldBe HttpStatus.NO_CONTENT
        }

        val client = backgrounds.clients.getAllClients().singleOrNull()

        "создавать клиента с корректным значениями карточки клиента" {
            client shouldMatch entranceSurveyJson
        }

        "заполнять поле 'Жалобы' ответом из соответствующего поля" {
            client?.complaints shouldContain entranceSurveyJson["survey"]["answer"]["data"][Survey.COMPLAINTS_FIELD]["value"].textValue()
        }

        "заполнять поле 'Анамнез' карточки клиента корректным строковым представлением ответов на пользовательские вопросы" {
            client?.anamnesis shouldContainAllCustomAnswersFrom entranceSurveyJson
        }
    }

    "при отправке запроса только с телефоном и именем клиента должна " - {
        // Сетап
        val phoneAndNameOnlySurveyJsonStr = phoneAndNameOnlySurveyJsonStr(THE_ADMIN_LOGIN)
        val phoneAndNameOnlyEntranceSurveyJson = jacksonObjectMapper().readTree(phoneAndNameOnlySurveyJsonStr)

        val yandexAdminEmail = phoneAndNameOnlyEntranceSurveyJson["yandexAdminEmail"].textValue()
        backgrounds.settingsBackgrounds.createSurveyFormsSettings(
            THE_THERAPIST_REF,
            SurveyFormsSettingsObjectMother.aSurveyFromsSettingsFrom(yandexAdminEmail)
        )

        // Действие
        val status = yandexFormsClient.createSurvey(phoneAndNameOnlySurveyJsonStr)

        // Проверка
        "возвращать статус 204" {
            status shouldBe HttpStatus.NO_CONTENT
        }

        val client = backgrounds.clients.getAllClients().singleOrNull()

        "создавать клиента с корректным значениями карточки клиента" {
            client shouldMatch phoneAndNameOnlyEntranceSurveyJson
        }

        "оставлять поле 'Жалобы' пустым" {
            client!!.complaints shouldBe null
        }

        "оставлять поле 'Анамнез' пустым" {
            client!!.anamnesis shouldBe null
        }

    }

    "при отправке существующим клиентом запроса с жалобами и кастомными вопросами должна" - {

        // Сетап
        val (settingsFixture, clientFixture) = presets.surveysFixturePresets.createSingleClient(clientDto = ClientsObjectMother.createClientCardDto())
        val customQuestion = "Были ли травмы"
        val customAnswer = "В тесте обновления жалоб и анамнеза травм у меня не было"
        val complaints = "Мои жалобы для теста обновления жалоб существующего клинета"
        val requestBody = complaintsAndCustomQuestionSurveyJsonStr(
            settingsFixture.yandexAdminEmail,
            clientFixture.clientPhone.toUIFormat(),
            complaints,
            customQuestion,
            customAnswer
        )

        // Действие
        val status = yandexFormsClient.createSurvey(requestBody)

        // Проверка
        "возвращать статус 204" {
            status shouldBe HttpStatus.NO_CONTENT
        }

        val client = backgrounds.clients.getAllClients().singleOrNull()!!

        "дополнять поле Жалобы" {
            client.complaints shouldContain complaints
            client.complaints shouldContain clientFixture.complaints!!
        }

        "дополнять поле Анамнез" {
            client.anamnesis shouldContain customQuestion
            client.anamnesis shouldContain customAnswer
            client.anamnesis shouldContain clientFixture.anamnesis!!
        }
    }

    "при отправке запроса с полем типа 'один вариант' должна" - {
        // Сетап

        val (settingsFixture, clientFixture) = presets.surveysFixturePresets.createSingleClient(clientDto = ClientsObjectMother.createClientCardDto())
        val question = "Вопрос теста рендеринга одного варианта"
        val answer = "Ответ теста рендеринга одного варианта"
        val yandexAdminEmail = settingsFixture.yandexAdminEmail
        val requestBody = singleChoiceSurveyJsonStr(
            yandexAdminEmail,
            clientFixture.clientPhone.toUIFormat(),
            question,
            answer
        )

        backgrounds.settingsBackgrounds.createSurveyFormsSettings(
            THE_THERAPIST_REF,
            SurveyFormsSettingsObjectMother.aSurveyFromsSettingsFrom(yandexAdminEmail)
        )

        // Действие
        val status = yandexFormsClient.createSurvey(requestBody)

        // Проверка
        "возвращать статус 204" {
            status shouldBe HttpStatus.NO_CONTENT
        }

        val client = backgrounds.clients.getAllClients().singleOrNull()!!

        "корректно рендерить его в строковое представление анкеты" {
            client.anamnesis shouldContain question
            client.anamnesis shouldContain answer
        }

    }

    "при отправке запроса с полем типа 'выбор из списка' должна" - {

        // Сетап

        val (settingsFixture, clientFixture) = presets.surveysFixturePresets.createSingleClient(clientDto = ClientsObjectMother.createClientCardDto())
        val question = "Вопрос теста рендеринга нескольких вариантов"
        val answers = listOf("Вариант 1", "Вариант 2")
        val yandexAdminEmail = settingsFixture.yandexAdminEmail
        val requestBody = multipleChoiceSurveyJsonStr(
            yandexAdminEmail,
            clientFixture.clientPhone.toUIFormat(),
            question,
            answers
        )

        backgrounds.settingsBackgrounds.createSurveyFormsSettings(
            THE_THERAPIST_REF,
            SurveyFormsSettingsObjectMother.aSurveyFromsSettingsFrom(yandexAdminEmail)
        )

        // Действие
        val status = yandexFormsClient.createSurvey(requestBody)

        // Проверка
        "возвращать статус 204" {
            status shouldBe HttpStatus.NO_CONTENT
        }

        val client = backgrounds.clients.getAllClients().singleOrNull()!!

        "корректно рендерить его в строковое представление анкеты" {
            client.anamnesis shouldContain question
            answers.forEach { answer -> client.anamnesis shouldContain answer }
        }

    }

    "при отправке запроса с yandexAdminEmail не указанного в настройках ни одного терапевта должна возвращать ошибку 409" - {
        // Сетап
        val requestBody = phoneAndNameOnlySurveyJsonStr(faker.internet().emailAddress())

        // Действие
        val errorResponse = yandexFormsClient.createSurveyForError(requestBody)

        // Проверка
        errorResponse.status shouldBe HttpStatus.CONFLICT.value()
        errorResponse.type.path shouldBe InvalidSurveyException.SURVEY_SETTINGS_NOT_FOUND_FOR_ADMIN_EMAIL
    }

})

private infix fun Client?.shouldMatch(surveyRqJson: JsonNode) {
    this shouldNotBe null
    this!!
    val answerData = surveyRqJson["survey"]["answer"]["data"]
    phoneNumber shouldBe PhoneNumber.of(answerData[Survey.PHONE_NUMBER_FIELD]["value"].textValue())
    firstName shouldBe answerData[Survey.FIRST_NAME_FIELD]["value"].textValue()
    lastName shouldBe answerData[Survey.LAST_NAME_FIELD]["value"].textValue()
    middleName shouldBe answerData[Survey.MIDDLE_NAME_FIELD]?.get("value")?.textValue()
    birthDate?.toString() shouldBe answerData[Survey.BIRTH_DATE_FIELD]?.get("value")?.textValue()
    address shouldBe answerData[Survey.LOCATION_FIELD]?.get("value")?.get(0)?.get("text")?.textValue()
}

private infix fun String?.shouldContainAllCustomAnswersFrom(surveyRqJson: JsonNode) {
    this shouldNotBe null
    this!!
    val answerData = surveyRqJson["survey"]["answer"]["data"] as ObjectNode
    answerData.properties()
        .filter { it.key !in Survey.standardFields }
        .forAll {
            this shouldContain it.value.asText()
        }
}