package pro.qyoga.clients.api

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.qyoga.clients.pages.therapist.clients.ClientsListPage
import pro.qyoga.core.clients.api.ClientSearchDto


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

    fun searchClients(searchForm: ClientSearchDto): Document {
        return Given {
            authorized()
            formParam(ClientsListPage.ClientSearchForm.firstName.name, searchForm.firstName)
            formParam(ClientsListPage.ClientSearchForm.lastName.name, searchForm.lastName)
            formParam(ClientsListPage.ClientSearchForm.patronymic.name, searchForm.middleName)
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