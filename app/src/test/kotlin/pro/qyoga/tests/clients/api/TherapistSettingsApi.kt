package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.springframework.http.HttpStatus
import pro.qyoga.app.therapist.account.SettingsPageController
import pro.qyoga.app.therapist.survey_forms.settings.SurveyFormsSettingsComponentController
import pro.qyoga.app.therapist.survey_forms.settings.SurveyFormsSettingsForm
import pro.qyoga.tests.infra.rest_assured.addResponseLogging
import pro.qyoga.tests.pages.therapist.survey_forms.SurveyFormsSettingsComponent


class TherapistSettingsApi(override val authCookie: Cookie) : AuthorizedApi {

    fun getSettingsPage(): Document {
        return Given {
            authorized()
        } When {
            get(SettingsPageController.PATH)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun getSurveyFormsSettingsComponent(): Element {
        return Given {
            authorized()
        } When {
            get(SurveyFormsSettingsComponentController.PATH)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString()).select("body").first()!!.child(0)
        }
    }

    fun updateSurveyFormsSettings(form: SurveyFormsSettingsForm): Element {
        return Given {
            authorized()
            formParam(SurveyFormsSettingsComponent.SurveyFormsSettingsForm.yandexAdminEmail.name, form.yandexAdminEmail)
            addResponseLogging()
        } When {
            put(SurveyFormsSettingsComponentController.PATH)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString()).select("body").first()!!.child(0)
        }
    }

}