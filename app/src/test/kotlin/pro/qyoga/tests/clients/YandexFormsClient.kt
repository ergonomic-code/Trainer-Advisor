package pro.qyoga.tests.clients

import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import pro.qyoga.app.publc.surverys.SurveysController


class YandexFormsClient(
    private val client: WebTestClient
) {

    fun createSurvey(entranceSurveyJsonStr: String): HttpStatusCode {
        return client
            .post()
            .uri(SurveysController.PATH)
            .bodyValue(entranceSurveyJsonStr)
            .exchange()
            .returnResult<ResponseEntity<String>>()
            .status
    }

}