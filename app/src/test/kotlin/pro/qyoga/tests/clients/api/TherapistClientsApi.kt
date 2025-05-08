package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.specification.RequestSpecification
import org.hamcrest.CoreMatchers.endsWith
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.qyoga.app.therapist.clients.components.ClientsComboBoxController
import pro.qyoga.app.therapist.clients.list.ClientsListPageController
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.dtos.ClientSearchDto
import pro.qyoga.tests.pages.therapist.clients.ClientsListPage
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientForm
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientPage
import pro.qyoga.tests.pages.therapist.clients.card.EditClientForm
import pro.qyoga.tests.pages.therapist.clients.card.EditClientPage
import java.util.*


class TherapistClientsApi(override val authCookie: Cookie) : AuthorizedApi {

    fun getClientsListPage(): Document {
        return Given {
            authorized()
        } When {
            get(ClientsListPage.path)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun getCreateClientPage(): Document {
        return Given {
            authorized()
        } When {
            get(CreateClientPage.path)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun getClientEditPage(clientId: UUID, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return Given {
            authorized()
            pathParam("id", clientId)
        } When {
            get(ClientsListPage.updateAction)
        } Then {
            statusCode(expectedStatus.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun getEditClientCardPage(clientId: UUID, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return Given {
            authorized()
            pathParam("clientId", clientId)
        } When {
            get(EditClientPage.PATH)
        } Then {
            statusCode(expectedStatus.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun createClient(request: ClientCardDto) {
        Given {
            authorized()
            clientCardFormParams(request)
        } When {
            post(CreateClientForm.action.url)
        } Then {
            statusCode(HttpStatus.OK.value())
            header("HX-Redirect", endsWith(ClientsListPage.path))
            header("HX-Trigger", "formSaved")
        }
    }

    fun createClientForError(request: ClientCardDto): Document {
        return Given {
            authorized()
            clientCardFormParams(request)
        } When {
            post(CreateClientForm.action.url)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun editClient(clientId: UUID, request: ClientCardDto) {
        Given {
            authorized()
            pathParam("clientId", clientId)
            clientCardFormParams(request)
        } When {
            post(EditClientPage.PATH)
        } Then {
            statusCode(HttpStatus.OK.value())
            header("HX-Redirect", endsWith(ClientsListPage.path))
            header("HX-Trigger", "formSaved")
        }
    }

    fun editClientForError(
        clientId: UUID,
        request: ClientCardDto,
        expectedStatus: HttpStatus = HttpStatus.OK
    ): Document {
        return Given {
            authorized()
            pathParam("clientId", clientId)
            clientCardFormParams(request)
        } When {
            post(EditClientPage.PATH)
        } Then {
            statusCode(expectedStatus.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    private fun RequestSpecification.clientCardFormParams(request: ClientCardDto): RequestSpecification {
        formParam(EditClientForm.firstName.name, request.firstName)
        formParam(EditClientForm.lastName.name, request.lastName)
        formParam(EditClientForm.middleName.name, request.middleName ?: "")
        formParam(EditClientForm.birthDate.name, request.birthDate?.toString() ?: "")
        formParam(EditClientForm.email.name, request.email ?: "")
        formParam(EditClientForm.phoneNumber.name, request.phoneNumber)
        formParam(EditClientForm.address.name, request.address ?: "")
        formParam(EditClientForm.complaints.name, request.complaints)
        formParam(EditClientForm.anamnesis.name, request.anamnesis ?: "")
        formParam(EditClientForm.distributionSourceType.name, request.distributionSourceType?.name ?: "")
        formParam(EditClientForm.distributionSourceComment.name, request.distributionSourceComment ?: "")
        return formParam(EditClientForm.version.name, request.version)
    }

    fun searchClients(searchForm: ClientSearchDto = ClientSearchDto(), page: Int = 1): Document {
        return Given {
            authorized()
            formParam(ClientsListPage.ClientSearchForm.firstName.name, searchForm.firstName)
            formParam(ClientsListPage.ClientSearchForm.lastName.name, searchForm.lastName)
            formParam(ClientsListPage.ClientSearchForm.phoneNumber.name, searchForm.phoneNumber)
            formParam(ClientsListPageController.SEARCH_PARAM_PAGE, page - 1)
        } When {
            get(ClientsListPageController.SEARCH_PATH)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun deleteClient(clientId: UUID) {
        Given {
            authorized()
            pathParam("id", clientId)
        } When {
            delete(ClientsListPage.DELETE_ACTION)
        } Then {
            statusCode(HttpStatus.OK.value())
        }
    }

    fun autocompleteClients(searchKey: String?): Document {
        return Given {
            authorized()
            if (searchKey != null) {
                queryParam("clientTitle", searchKey)
            }
            this
        } When {
            get(ClientsComboBoxController.PATH)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

}