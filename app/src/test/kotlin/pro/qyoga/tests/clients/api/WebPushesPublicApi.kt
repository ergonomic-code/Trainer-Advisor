package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.returnResult
import pro.qyoga.app.publc.pushes.web.PushesPublicKeyController
import pro.qyoga.tests.infra.web.mainRestTestClient


object WebPushesApiFactory {

    val publicApi = WebPushesPublicApi(mainRestTestClient)

    fun therapistApi(
        principal: Cookie,
    ) = WebPushesTherapistApi(principal, mainRestTestClient)

}

@Suppress("UnusedReceiverParameter")
val TrainerAdvisorApis.WebPushes
    get() = WebPushesApiFactory

class WebPushesPublicApi(
    private val client: RestTestClient
) {

    fun getPublicKey(): String {
        return client.get()
            .uri(PushesPublicKeyController.PUBLIC_KEY_PATH)
            .exchange()
            .expectStatus().isOk
            .returnResult<String>()
            .responseBody ?: ""
    }

}
