package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters.fromValue
import pro.qyoga.app.pushes.web.WebPushesController
import pro.qyoga.i9ns.pushes.web.WebPushSubscription

class WebPushesTherapistApi(
    override val authCookie: Cookie,
    private val webTestClient: WebTestClient
) : AuthorizedApi {

    fun createSubscription(subscription: WebPushSubscription) {
        webTestClient.post()
            .uri(WebPushesController.PATH)
            .body(fromValue(subscription))
            .authorized()
            .exchange()
            .expectStatus().isNoContent
    }

    fun deleteSubscription(p256dh: String) {
        webTestClient.delete()
            .uri(WebPushesController.DELETE_SUBSCRIPTION_PATH, p256dh)
            .authorized()
            .exchange()
            .expectStatus().isNoContent
    }

}
