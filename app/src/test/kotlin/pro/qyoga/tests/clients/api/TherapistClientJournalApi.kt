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
import pro.qyoga.app.therapist.clients.journal.edit_entry.create.CreateJournalEntryPageController
import pro.qyoga.app.therapist.clients.journal.list.JournalPageController
import pro.qyoga.core.clients.journals.dtos.EditJournalEntryRq
import pro.qyoga.tests.pages.therapist.clients.journal.entry.CreateJournalEntryForm
import pro.qyoga.tests.pages.therapist.clients.journal.entry.EditJournalEntryPage
import pro.qyoga.tests.platform.pathToRegex
import java.util.*


class TherapistClientJournalApi(override val authCookie: Cookie) : AuthorizedApi {

    fun getCreateJournalEntryPage(clientId: UUID, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return Given {
            authorized()
            pathParam("clientId", clientId)
        } When {
            get(CreateJournalEntryPageController.CREATE_JOURNAL_PAGE_URL)
        } Then {
            statusCode(expectedStatus.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun getEditJournalEntryPage(clientId: UUID, entryId: Long, expectedStatus: HttpStatus = HttpStatus.OK): Document {
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

    fun getJournalPage(clientId: UUID): Document {
        return Given {
            authorized()
            pathParam("clientId", clientId)
        } When {
            get(JournalPageController.JOURNAL_PAGE_PATH)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun createJournalEntry(clientId: UUID, journalEntry: EditJournalEntryRq) {
        postNewJournalEntry(journalEntry, clientId) Then {
            statusCode(HttpStatus.OK.value())
            header("Hx-Redirect", Matchers.matchesRegex(".*" + JournalPageController.JOURNAL_PAGE_PATH.pathToRegex()))
        }
    }

    fun createJournalEntryForError(
        clientId: UUID,
        journalEntry: EditJournalEntryRq,
        expectedStatus: HttpStatus = HttpStatus.OK
    ): Document {
        return postNewJournalEntry(journalEntry, clientId) Then {
            statusCode(expectedStatus.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    private fun postNewJournalEntry(
        journalEntry: EditJournalEntryRq,
        clientId: UUID
    ) = Given {
        authorized()
        formParam(CreateJournalEntryForm.version.name, journalEntry.version)
        formParam(CreateJournalEntryForm.dateInput.name, journalEntry.date.toString())
        formParam(
            CreateJournalEntryForm.therapeuticTaskNameInput.name,
            journalEntry.therapeuticTaskName
        )
        formParam(CreateJournalEntryForm.entryTextInput.name, journalEntry.journalEntryText)
        pathParam("clientId", clientId)
    } When {
        post(CreateJournalEntryPageController.CREATE_JOURNAL_PAGE_URL)
    }

    fun editJournalEntry(
        clientId: UUID,
        entryId: Long,
        journalEntry: EditJournalEntryRq,
    ) {
        return postJournalEntryEdit(clientId, entryId, journalEntry) Then {
            statusCode(HttpStatus.OK.value())
            header("Hx-Redirect", Matchers.matchesRegex(".*" + JournalPageController.JOURNAL_PAGE_PATH.pathToRegex()))
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun editJournalEntryForError(
        clientId: UUID,
        entryId: Long,
        journalEntry: EditJournalEntryRq,
        expectedStatus: HttpStatus = HttpStatus.OK
    ): Document {
        return postJournalEntryEdit(clientId, entryId, journalEntry) Then {
            statusCode(expectedStatus.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    private fun postJournalEntryEdit(
        clientId: UUID,
        entryId: Long,
        journalEntry: EditJournalEntryRq,
    ) = Given {
        authorized()
        formParam(CreateJournalEntryForm.version.name, journalEntry.version)
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

    fun deleteEntry(clientId: UUID, entryId: Long, expectedStatus: HttpStatus = HttpStatus.OK) {
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