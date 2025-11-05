package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import pro.qyoga.app.publc.pushes.web.PushesPublicKeyController
import pro.qyoga.tests.infra.web.mainWebTestClient


object WebPushesApiFactory {

    val publicApi = WebPushesPublicApi(mainWebTestClient)

    fun therapistApi(
        principal: Cookie,
    ) = WebPushesTherapistApi(principal, mainWebTestClient)

}

@Suppress("UnusedReceiverParameter")
val TrainerAdvisorApis.WebPushes
    get() = WebPushesApiFactory

class WebPushesPublicApi(
    private val client: WebTestClient
) {

    fun getPublicKey(): String {
        return client.get()
            .uri(PushesPublicKeyController.PUBLIC_KEY_PATH)
            .exchange()
            .expectStatus().isOk
            .returnResult<String>()
            .responseBody
            .blockFirst() ?: ""
    }

}
