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
import pro.qyoga.core.clients.journals.model.EditJournalEntryRequest
import pro.qyoga.tests.pages.therapist.clients.journal.entry.CreateJournalEntryForm
import pro.qyoga.tests.pages.therapist.clients.journal.entry.CreateJournalEntryPage
import pro.qyoga.tests.pages.therapist.clients.journal.entry.EditJournalEntryPage
import pro.qyoga.tests.pages.therapist.clients.journal.list.EmptyClientJournalPage
import pro.qyoga.tests.platform.pathToRegex


class TherapistClientJournalApi(override val authCookie: Cookie) : AuthorizedApi {

    fun getCreateJournalEntryPage(clientId: Long, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return Given {
            authorized()
            pathParam("id", clientId)
        } When {
            get(CreateJournalEntryPage.PATH)
        } Then {
            statusCode(expectedStatus.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun getEditJournalEntryPage(clientId: Long, entryId: Long, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return Given {
            authorized()
            pathParam("clientId", clientId)
            pathParam("entryId", entryId)
        } When {
            get(EditJournalEntryPage.PATH)
        } Then {
            statusCode(expectedStatus.value())
        } Extract {
            Jsoup.parse(body().asString())
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

    fun createJournalEntry(clientId: Long, journalEntry: EditJournalEntryRequest) {
        postNewJournalEntry(journalEntry, clientId) Then {
            statusCode(HttpStatus.OK.value())
            header("Hx-Redirect", Matchers.matchesRegex(".*" + EmptyClientJournalPage.path.pathToRegex()))
        }
    }

    fun createJournalEntryForError(
        clientId: Long,
        journalEntry: EditJournalEntryRequest,
        expectedStatus: HttpStatus = HttpStatus.OK
    ): Document {
        return postNewJournalEntry(journalEntry, clientId) Then {
            statusCode(expectedStatus.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    private fun postNewJournalEntry(
        journalEntry: EditJournalEntryRequest,
        clientId: Long
    ) = Given {
        authorized()
        formParam(CreateJournalEntryForm.dateInput.name, journalEntry.date.toString())
        formParam(
            CreateJournalEntryForm.therapeuticTaskNameInput.name,
            journalEntry.therapeuticTaskName
        )
        formParam(CreateJournalEntryForm.entryTextInput.name, journalEntry.journalEntryText)
        pathParam("id", clientId)
    } When {
        post(CreateJournalEntryPage.PATH)
    }

    fun editJournalEntry(
        clientId: Long,
        entryId: Long,
        journalEntry: EditJournalEntryRequest,
    ) {
        return postJournalEntryEdit(clientId, entryId, journalEntry) Then {
            statusCode(HttpStatus.OK.value())
            header("Hx-Redirect", Matchers.matchesRegex(".*" + EmptyClientJournalPage.path.pathToRegex()))
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun editJournalEntryForError(
        clientId: Long,
        entryId: Long,
        journalEntry: EditJournalEntryRequest,
        expectedStatus: HttpStatus = HttpStatus.OK
    ): Document {
        return postJournalEntryEdit(clientId, entryId, journalEntry) Then {
            statusCode(expectedStatus.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    private fun postJournalEntryEdit(
        clientId: Long,
        entryId: Long,
        journalEntry: EditJournalEntryRequest,
    ) = Given {
        authorized()
        formParam(CreateJournalEntryForm.dateInput.name, journalEntry.date.toString())
        formParam(
            CreateJournalEntryForm.therapeuticTaskNameInput.name,
            journalEntry.therapeuticTaskName
        )
        formParam(CreateJournalEntryForm.entryTextInput.name, journalEntry.journalEntryText)
        pathParam("clientId", clientId)
        pathParam("entryId", entryId)
    } When {
        post(EditJournalEntryPage.PATH)
    }

    fun deleteEntry(clientId: Long, entryId: Long, expectedStatus: HttpStatus = HttpStatus.OK) {
        Given {
            authorized()
            pathParam("clientId", clientId)
            pathParam("entryId", entryId)
        } When {
            delete(EditJournalEntryPage.PATH)
        } Then {
            statusCode(expectedStatus.value())
        }
    }

}