package pro.qyoga.tests.clients.api

import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import pro.qyoga.app.publc.pushes.web.PushesPublicKeyController


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
