package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import org.springframework.test.web.servlet.client.RestTestClient
import pro.qyoga.app.pushes.web.WebPushesController
import pro.qyoga.i9ns.pushes.web.model.WebPushSubscription

class WebPushesTherapistApi(
    override val authCookie: Cookie,
    private val restTestClient: RestTestClient
) : AuthorizedApi {

    fun createSubscription(subscription: WebPushSubscription) {
        restTestClient.post()
            .uri(WebPushesController.PATH)
            .body(subscription)
            .authorized()
            .exchange()
            .expectStatus().isNoContent
    }

    fun deleteSubscription(p256dh: String) {
        restTestClient.delete()
            .uri(WebPushesController.DELETE_SUBSCRIPTION_PATH, p256dh)
            .authorized()
            .exchange()
            .expectStatus().isNoContent
    }

}
