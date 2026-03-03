package pro.qyoga.tests.clients

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.returnResult
import pro.azhidkov.platform.spring.http.ErrorResponse
import pro.qyoga.app.publc.surverys.SurveysController


class YandexFormsClient(
    private val client: RestTestClient
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
            .responseBody!!
    }

    fun createSurveyForResponse(entranceSurveyJsonStr: String): RestTestClient.ResponseSpec {
        return client
            .post()
            .uri(SurveysController.PATH)
            .body(entranceSurveyJsonStr)
            .exchange()
    }

}
