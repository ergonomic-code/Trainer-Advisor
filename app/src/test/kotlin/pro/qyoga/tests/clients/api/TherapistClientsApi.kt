package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.endsWith
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.qyoga.app.therapist.clients.components.ClientsComboBoxController
import pro.qyoga.core.clients.cards.api.ClientCardDto
import pro.qyoga.core.clients.cards.api.ClientSearchDto
import pro.qyoga.tests.pages.therapist.clients.ClientsListPage
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientForm
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientPage
import pro.qyoga.tests.pages.therapist.clients.card.EditClientForm
import pro.qyoga.tests.pages.therapist.clients.card.EditClientPage


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

    fun getClientEditPage(clientId: Long, expectedStatus: HttpStatus = HttpStatus.OK): Document {
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

    fun getEditClientCardPage(clientId: Long, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return Given {
            authorized()
            pathParam("id", clientId)
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
            formParam(CreateClientForm.firstName.name, request.firstName)
            formParam(CreateClientForm.lastName.name, request.lastName)
            formParam(CreateClientForm.middleName.name, request.middleName ?: "")
            formParam(CreateClientForm.birthDate.name, request.birthDate?.toString() ?: "")
            formParam(CreateClientForm.email.name, request.email ?: "")
            formParam(CreateClientForm.phoneNumber.name, request.phoneNumber)
            formParam(CreateClientForm.address.name, request.address ?: "")
            formParam(CreateClientForm.complaints.name, request.complaints ?: "")
            formParam(CreateClientForm.anamnesis.name, request.anamnesis ?: "")
            formParam(CreateClientForm.distributionSourceType.name, request.distributionSourceType ?: "")
            formParam(CreateClientForm.distributionSourceComment.name, request.distributionSourceComment ?: "")
        } When {
            post(CreateClientForm.action.url)
        } Then {
            statusCode(HttpStatus.FOUND.value())
            header("Location", endsWith(ClientsListPage.path))
        }
    }

    fun editClient(clientId: Long, request: ClientCardDto) {
        Given {
            authorized()

            pathParam("id", clientId)

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
        } When {
            post(EditClientPage.PATH)
        } Then {
            statusCode(HttpStatus.FOUND.value())
            header("Location", endsWith(ClientsListPage.path))
        }
    }

    fun searchClients(searchForm: ClientSearchDto): Document {
        return Given {
            authorized()
            formParam(ClientsListPage.ClientSearchForm.firstName.name, searchForm.firstName)
            formParam(ClientsListPage.ClientSearchForm.lastName.name, searchForm.lastName)
            formParam(ClientsListPage.ClientSearchForm.phoneNumber.name, searchForm.phoneNumber)
        } When {
            get(ClientsListPage.ClientSearchForm.action.url)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun deleteClient(id: Long) {
        Given {
            authorized()
            pathParam("id", id)
        } When {
            delete(ClientsListPage.deleteAction)
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