package pro.qyoga.tests.clients

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import pro.azhidkov.platform.spring.http.ErrorResponse
import pro.qyoga.app.publc.surverys.SurveysController


class YandexFormsClient(
    private val client: WebTestClient
) {

    fun createSurvey(surveyJsonStr: String): HttpStatusCode {
        return createSurveyForResponse(surveyJsonStr)
            .returnResult<ResponseEntity<String>>()
            .status
    }

    fun createSurveyForError(
        entranceSurveyJsonStr: String,
        expectedStatus: HttpStatus = HttpStatus.CONFLICT
    ): ErrorResponse {
        return createSurveyForResponse(entranceSurveyJsonStr)
            .expectStatus().isEqualTo(expectedStatus)
            .returnResult(ErrorResponse::class.java)
            .responseBody
            .blockFirst()!!
    }

    fun createSurveyForResponse(entranceSurveyJsonStr: String): WebTestClient.ResponseSpec {
        return client
            .post()
            .uri(SurveysController.PATH)
            .bodyValue(entranceSurveyJsonStr)
            .exchange()
    }

}