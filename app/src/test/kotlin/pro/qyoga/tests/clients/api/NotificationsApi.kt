package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.test.web.servlet.client.RestTestClient
import pro.qyoga.app.therapist.appointments.core.schedule.settings.NotificationsSettingsController
import pro.qyoga.tests.infra.web.mainRestTestClient
import pro.qyoga.tests.platform.spring.rest_test_client.getBodyAsString

object NotificationsApiFactory {

    fun therapistApi(
        principal: Cookie,
    ) = NotificationsTherapistApi(principal, mainRestTestClient)

}

@Suppress("UnusedReceiverParameter")
val TrainerAdvisorApis.Notifications
    get() = NotificationsApiFactory

class NotificationsTherapistApi(
    override val authCookie: Cookie,
    private val restTestClient: RestTestClient
) : AuthorizedApi {

    fun getNotificationsSettings(): Document {
        return restTestClient.get()
            .uri(NotificationsSettingsController.PATH)
            .authorized()
            .exchange()
            .expectStatus().isOk
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

}
