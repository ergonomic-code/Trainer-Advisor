package pro.qyoga.tests.clients.api

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpCookie
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient
import pro.qyoga.app.therapist.appointments.types.components.AppointmentTypesComboBoxController
import pro.qyoga.tests.infra.web.pageRestTestClient
import pro.qyoga.tests.platform.spring.rest_test_client.getBodyAsString


class TherapistAppointmentTypesApi(
    override val authCookie: HttpCookie,
    private val restTestClient: RestTestClient = pageRestTestClient
) : AuthorizedApi {

    fun autocompleteSearch(searchKey: String?): Document {
        return restTestClient.get()
            .uri { builder ->
                builder.path(AppointmentTypesComboBoxController.PATH).apply {
                    if (searchKey != null) {
                        queryParam("appointmentTypeTitle", searchKey)
                    }
                }.build()
            }
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

}