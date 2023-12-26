package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.qyoga.tests.clients.pages.therapist.clients.journal.entry.CreateJournalEntryForm
import pro.qyoga.tests.clients.pages.therapist.clients.journal.entry.TherapeuticTasksSearchResult
import pro.qyoga.tests.clients.pages.therapist.therapy.therapeutic_tasks.TherapeuticTasksListPage

class TherapistTherapeuticTasksApi(override val authCookie: Cookie) : AuthorizedApi {

    fun search(searchKey: String): Document {
        return Given {
            authorized()
            queryParams(TherapeuticTasksListPage.SearchTasksForm.searchKey.name, searchKey)
        } When {
            get(TherapeuticTasksListPage.SearchTasksForm.action.url)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun autocompleteSearch(searchKey: String): Document {
        return Given {
            authorized()
            queryParams(CreateJournalEntryForm.therapeuticTaskNameInput.name, searchKey)
        } When {
            get(TherapeuticTasksSearchResult.PATH)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun getTasksListPage(): Document {
        return Given {
            authorized()
        } When {
            get(TherapeuticTasksListPage.path)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

}
