package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.test.web.reactive.server.WebTestClient
import pro.qyoga.app.therapist.appointments.core.schedule.settings.NotificationsSettingsController
import pro.qyoga.tests.infra.web.mainWebTestClient
import pro.qyoga.tests.platform.spring.web_test_client.getBodyAsString

object Notifications {

    fun therapistApi(
        principal: Cookie,
    ) = NotificationsTherapistApi(principal, mainWebTestClient)

}

@Suppress("UnusedReceiverParameter", "RemoveRedundantQualifierName")
val TrainerAdvisorApis.Notifications
    get() = pro.qyoga.tests.clients.api.Notifications

class NotificationsTherapistApi(
    override val authCookie: Cookie,
    private val webTestClient: WebTestClient
) : AuthorizedApi {

    fun getNotificationsSettings(): Document {
        return webTestClient.get()
            .uri(NotificationsSettingsController.PATH)
            .authorized()
            .exchange()
            .expectStatus().isOk
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

}
