package pro.qyoga.tests.clients.api

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.springframework.http.HttpCookie
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient
import pro.qyoga.app.therapist.account.SettingsPageController
import pro.qyoga.app.therapist.survey_forms.settings.SurveyFormsSettingsComponentController
import pro.qyoga.app.therapist.survey_forms.settings.SurveyFormsSettingsForm
import pro.qyoga.tests.infra.web.pageRestTestClient
import pro.qyoga.tests.pages.therapist.survey_forms.SurveyFormsSettingsComponent
import pro.qyoga.tests.platform.spring.rest_test_client.formData
import pro.qyoga.tests.platform.spring.rest_test_client.getBodyAsString


class TherapistSettingsApi(
    override val authCookie: HttpCookie,
    private val restTestClient: RestTestClient = pageRestTestClient
) : AuthorizedApi {

    fun getSettingsPage(): Document {
        return restTestClient.get()
            .uri(SettingsPageController.PATH)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun getSurveyFormsSettingsComponent(): Element {
        return restTestClient.get()
            .uri(SurveyFormsSettingsComponentController.PATH)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it).select("body").first()!!.child(0) }
    }

    fun updateSurveyFormsSettings(form: SurveyFormsSettingsForm): Element {
        return restTestClient.put()
            .uri(SurveyFormsSettingsComponentController.PATH)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formData(
                SurveyFormsSettingsComponent.SurveyFormsSettingsForm.yandexAdminEmail.name to form.yandexAdminEmail,
            ))
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it).select("body").first()!!.child(0) }
    }

}