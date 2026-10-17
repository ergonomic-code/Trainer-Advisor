package pro.qyoga.tests.clients

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.returnResult
import pro.qyoga.tests.infra.web.mainRestTestClient
import tools.jackson.databind.JsonNode


val actuatorPath = "ops/actuator"

class OpsClient(
    private val login: String,
    private val password: String
) {

    fun getActuatorEntryPoint(expectedStatus: HttpStatus = HttpStatus.OK): JsonNode? {
        val response = getActuatorEntryPointForResponse()
            .expectStatus().isEqualTo(expectedStatus.value())
        return if (expectedStatus == HttpStatus.OK) {
            response.returnResult<JsonNode>().responseBody
        } else {
            null
        }
    }

    fun getActuatorEntryPointForResponse(): RestTestClient.ResponseSpec {
        return mainRestTestClient.get()
            .uri("/$actuatorPath")
            .headers { it.setBasicAuth(login, password) }
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
    }

}
