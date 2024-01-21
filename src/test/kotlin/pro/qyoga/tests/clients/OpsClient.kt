package pro.qyoga.tests.clients

import com.fasterxml.jackson.databind.JsonNode
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.springframework.http.HttpStatus

val actuatorPath = "ops/actuator"

class OpsClient(
    private val login: String,
    private val password: String
) {

    fun getActuatorEntryPoint(expectedStatus: HttpStatus = HttpStatus.OK): JsonNode? {
        return Given {
            auth().preemptive().basic(login, password)
            accept(ContentType.JSON)
        } When {
            get(actuatorPath)
        } Then {
            statusCode(expectedStatus.value())
        } Extract {
            if (statusCode() == HttpStatus.OK.value()) {
                body().`as`(JsonNode::class.java)
            } else {
                null
            }
        }
    }

}