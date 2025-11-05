package pro.qyoga.tests.fixture.wiremocks

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.http.RequestMethod
import com.github.tomakehurst.wiremock.stubbing.ServeEvent
import org.springframework.stereotype.Component
import pro.qyoga.tests.fixture.object_mothers.pushes.web.WebPushesObjectMother
import java.net.URI


private const val WEB_PUSH_PREFIX = "/webpush/"

@Component
class MockWebPushServer(
    private val wiremockServer: WireMockServer
) {

    fun aEndpoint(): String =
        wiremockServer.baseUrl() + WEB_PUSH_PREFIX + WebPushesObjectMother.aFcmRegistrationToken()

    fun getSentPushes(): List<ServeEvent> {
        return wiremockServer.allServeEvents
            .filter {
                it.wasMatched &&
                        it.request.method == RequestMethod.POST &&
                        it.request.url.startsWith(WEB_PUSH_PREFIX)
            }
    }

    inner class OnSendPush(private val endpoint: String) {

        fun returnsOk() {
            val endpointPath = URI(endpoint).path
            wiremockServer.stubFor(
                post(endpointPath)
                    .withHeader("Content-Type", equalTo("application/octet-stream"))
                    .withHeader("Authorization", matching("vapid t=.+"))
                    .withHeader("Crypto-Key", containing("p256ecdsa="))
                    .willReturn(
                        noContent()
                    )
            )
        }

    }

}
