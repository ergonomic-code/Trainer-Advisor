package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.Matchers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.qyoga.app.therapist.clients.journal.CreateJournalEntryRequest
import pro.qyoga.tests.clients.pages.therapist.clients.journal.CreateJournalEntryPage
import pro.qyoga.tests.clients.pages.therapist.clients.journal.EmptyClientJournalPage
import pro.qyoga.tests.platform.pathToRegex


class TherapistClientJournalApi(override val authCookie: Cookie) : AuthorizedApi {

    fun getCreateJournalEntryPage(clientId: Long): Document {
        return Given {
            authorized()
            pathParam("id", clientId)
        } When {
            get(CreateJournalEntryPage.path)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun createJournalEntry(clientId: Long, journalEntry: CreateJournalEntryRequest) {
        Given {
            authorized()
            formParam(CreateJournalEntryPage.JournalEntryFrom.dateInput.name, journalEntry.date.toString())
            formParam(
                CreateJournalEntryPage.JournalEntryFrom.therapeuticTaskNameInput.name,
                journalEntry.therapeuticTaskName
            )
            formParam(CreateJournalEntryPage.JournalEntryFrom.entryTextInput.name, journalEntry.journalEntryText)
            pathParam("id", clientId)
        } When {
            post(CreateJournalEntryPage.path)
        } Then {
            statusCode(HttpStatus.FOUND.value())
            header("Location", Matchers.matchesRegex(".*" + EmptyClientJournalPage.path.pathToRegex()))
        }
    }

    fun getJournalPage(clientId: Long): Document {
        return Given {
            authorized()
            pathParam("id", clientId)
        } When {
            get(EmptyClientJournalPage.path)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

}