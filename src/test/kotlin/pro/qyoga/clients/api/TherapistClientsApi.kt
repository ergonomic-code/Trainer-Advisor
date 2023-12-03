package pro.qyoga.clients.api

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.endsWith
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.qyoga.clients.pages.therapist.clients.ClientsListPage
import pro.qyoga.clients.pages.therapist.clients.CreateClientPage
import pro.qyoga.clients.pages.therapist.clients.EditClientPage
import pro.qyoga.clients.pages.therapist.clients.editClientPath
import pro.qyoga.core.clients.api.ClientCardDto
import pro.qyoga.core.clients.api.ClientSearchDto
import pro.qyoga.infra.rest_assured.addResponseLogging


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
            addResponseLogging()
            pathParam("id", clientId)
        } When {
            get(ClientsListPage.updateAction)
        } Then {
            statusCode(expectedStatus.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun createClient(request: ClientCardDto) {
        Given {
            authorized()
            formParam(CreateClientPage.clientForm.firstName.name, request.firstName)
            formParam(CreateClientPage.clientForm.lastName.name, request.lastName)
            formParam(CreateClientPage.clientForm.middleName.name, request.middleName)
            formParam(CreateClientPage.clientForm.birthDate.name, request.birthDate.toString())
            formParam(CreateClientPage.clientForm.email.name, request.email)
            formParam(CreateClientPage.clientForm.phoneNumber.name, request.phoneNumber)
            formParam(CreateClientPage.clientForm.address.name, request.address)
            formParam(CreateClientPage.clientForm.distributionSource.name, request.distributionSource)
            formParam(CreateClientPage.clientForm.complaints.name, request.complaints)
        } When {
            post(CreateClientPage.clientForm.action.url)
        } Then {
            statusCode(HttpStatus.FOUND.value())
            header("Location", endsWith(ClientsListPage.path))
        }
    }

    fun editClient(clientId: Long, request: ClientCardDto) {
        val editClientPage = EditClientPage(clientId)
        Given {
            authorized()

            pathParam("id", clientId)

            formParam(editClientPage.clientForm.firstName.name, request.firstName)
            formParam(editClientPage.clientForm.lastName.name, request.lastName)
            formParam(editClientPage.clientForm.middleName.name, request.middleName)
            formParam(editClientPage.clientForm.birthDate.name, request.birthDate.toString())
            formParam(editClientPage.clientForm.email.name, request.email)
            formParam(editClientPage.clientForm.phoneNumber.name, request.phoneNumber)
            formParam(editClientPage.clientForm.address.name, request.address)
            formParam(editClientPage.clientForm.distributionSource.name, request.distributionSource)
            formParam(editClientPage.clientForm.complaints.name, request.complaints)
        } When {
            post(editClientPath)
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
            formParam(ClientsListPage.ClientSearchForm.middleName.name, searchForm.middleName)
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

}