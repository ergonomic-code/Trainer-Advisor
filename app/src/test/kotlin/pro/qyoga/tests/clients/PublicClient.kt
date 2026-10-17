package pro.qyoga.tests.clients

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient
import pro.qyoga.app.publc.components.TimeZonesComboBoxController
import pro.qyoga.tests.clients.api.AuthApi
import pro.qyoga.tests.infra.web.mainRestTestClient
import pro.qyoga.tests.infra.web.pageRestTestClient
import pro.qyoga.tests.pages.therapist.clients.ClientsListPage
import pro.qyoga.tests.platform.spring.rest_test_client.getBodyAsString


object PublicClient {

    val authApi = AuthApi

    fun getIndexPage(): Document {
        return pageRestTestClient.get()
            .uri("/")
            .accept(MediaType.TEXT_HTML)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun getFailPage(): Document {
        return pageRestTestClient.get()
            .uri("/test/fail")
            .accept(MediaType.TEXT_HTML)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun getNotFoundPage(): Document {
        return pageRestTestClient.get()
            .uri("/test/not-found")
            .accept(MediaType.TEXT_HTML)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.NOT_FOUND)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun getClientsPageWithBasicAuth(login: String, password: String): Document {
        return pageRestTestClient.get()
            .uri(ClientsListPage.path)
            .headers { it.setBasicAuth(login, password) }
            .accept(MediaType.TEXT_HTML)
            .exchange()
            .expectStatus().isOk
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun getUnopenedPage(): Document {
        return pageRestTestClient.get()
            .uri("/it-isnt-opened")
            .accept(MediaType.TEXT_HTML)
            .exchange()
            .expectStatus().isOk
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun getActuatorEntryPointForResponse(): RestTestClient.ResponseSpec {
        return mainRestTestClient.get()
            .uri("/$actuatorPath")
            .accept(MediaType.TEXT_HTML)
            .exchange()
    }

    fun getFontForResponse(): RestTestClient.ResponseSpec {
        return mainRestTestClient.get()
            .uri("/fonts/inter/Inter-Regular.woff2")
            .accept(MediaType.ALL)
            .exchange()
    }

    fun getTimeZones(timeZoneId: String? = null, timeZoneTitle: String? = null): Document {
        return pageRestTestClient.get()
            .uri { builder ->
                builder.path(TimeZonesComboBoxController.PATH).apply {
                    if (timeZoneId != null) {
                            queryParam("timeZoneId", timeZoneId)
                    }
                    if (timeZoneTitle != null) {
                            queryParam("timeZoneTitle", timeZoneTitle)
                    }
                }.build()
            }
            .accept(MediaType.TEXT_HTML)
            .exchange()
            .expectStatus().isOk
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

}