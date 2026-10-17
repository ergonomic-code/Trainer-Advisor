package pro.qyoga.tests.clients.api

import io.kotest.matchers.string.shouldEndWith
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpCookie
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.util.MultiValueMap
import pro.qyoga.app.therapist.clients.components.ClientsComboBoxController
import pro.qyoga.app.therapist.clients.list.ClientsListPageController
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.dtos.ClientSearchDto
import pro.qyoga.tests.infra.web.pageRestTestClient
import pro.qyoga.tests.pages.therapist.clients.ClientsListPage
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientForm
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientPage
import pro.qyoga.tests.pages.therapist.clients.card.EditClientForm
import pro.qyoga.tests.pages.therapist.clients.card.EditClientPage
import pro.qyoga.tests.platform.spring.rest_test_client.formData
import pro.qyoga.tests.platform.spring.rest_test_client.getBodyAsString
import java.util.*


class TherapistClientsApi(
    override val authCookie: HttpCookie,
    private val restTestClient: RestTestClient = pageRestTestClient
) : AuthorizedApi {

    fun getClientsListPage(): Document {
        return restTestClient.get()
            .uri(ClientsListPage.path)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun getCreateClientPage(): Document {
        return restTestClient.get()
            .uri(CreateClientPage.path)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun getClientEditPage(clientId: UUID, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return restTestClient.get()
            .uri(ClientsListPage.updateAction, clientId)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(expectedStatus.value())
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun getEditClientCardPage(clientId: UUID, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return restTestClient.get()
            .uri(EditClientPage.PATH, clientId)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(expectedStatus.value())
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun createClient(request: ClientCardDto) {
        restTestClient.post()
            .uri(CreateClientForm.action.url)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(clientCardForm(request))
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .expectHeader().value("HX-Redirect") { it shouldEndWith ClientsListPage.path }
            .expectHeader().valueEquals("HX-Trigger", "formSaved")
    }

    fun createClientForError(request: ClientCardDto): Document {
        return restTestClient.post()
            .uri(CreateClientForm.action.url)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(clientCardForm(request))
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun editClient(clientId: UUID, request: ClientCardDto) {
        restTestClient.post()
            .uri(EditClientPage.PATH, clientId)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(clientCardForm(request))
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .expectHeader().value("HX-Redirect") { it shouldEndWith ClientsListPage.path }
            .expectHeader().valueEquals("HX-Trigger", "formSaved")
    }

    fun editClientForError(
        clientId: UUID,
        request: ClientCardDto,
        expectedStatus: HttpStatus = HttpStatus.OK
    ): Document {
        return restTestClient.post()
            .uri(EditClientPage.PATH, clientId)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(clientCardForm(request))
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(expectedStatus.value())
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    private fun clientCardForm(request: ClientCardDto): MultiValueMap<String, String> {
        return formData(
            EditClientForm.firstName.name to request.firstName,
            EditClientForm.lastName.name to request.lastName,
            EditClientForm.middleName.name to (request.middleName ?: ""),
            EditClientForm.birthDate.name to (request.birthDate?.toString() ?: ""),
            EditClientForm.email.name to (request.email ?: ""),
            EditClientForm.phoneNumber.name to request.phoneNumber,
            EditClientForm.address.name to (request.address ?: ""),
            EditClientForm.complaints.name to request.complaints,
            EditClientForm.anamnesis.name to (request.anamnesis ?: ""),
            EditClientForm.distributionSourceType.name to (request.distributionSourceType?.name ?: ""),
            EditClientForm.distributionSourceComment.name to (request.distributionSourceComment ?: ""),
            EditClientForm.version.name to request.version
        )
    }

    fun searchClients(searchForm: ClientSearchDto = ClientSearchDto(), page: Int = 1): Document {
        return restTestClient.get()
            .uri { builder ->
                builder.path(ClientsListPageController.SEARCH_PATH).queryParams(formData(
                    ClientsListPage.ClientSearchForm.firstName.name to searchForm.firstName,
                    ClientsListPage.ClientSearchForm.lastName.name to searchForm.lastName,
                    ClientsListPage.ClientSearchForm.phoneNumber.name to searchForm.phoneNumber,
                    ClientsListPageController.SEARCH_PARAM_PAGE to page - 1,
                )).build()
            }
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun deleteClient(clientId: UUID) {
        restTestClient.delete()
            .uri(ClientsListPage.DELETE_ACTION, clientId)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
    }

    fun autocompleteClients(searchKey: String?): Document {
        return restTestClient.get()
            .uri { builder ->
                builder.path(ClientsComboBoxController.PATH).apply {
                    if (searchKey != null) {
                        queryParam("clientTitle", searchKey)
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