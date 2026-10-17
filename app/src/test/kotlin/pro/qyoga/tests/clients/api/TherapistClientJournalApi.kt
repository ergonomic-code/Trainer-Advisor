package pro.qyoga.tests.clients.api

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpCookie
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient
import pro.qyoga.app.therapist.clients.journal.edit_entry.create.CreateJournalEntryPageController
import pro.qyoga.app.therapist.clients.journal.list.JournalPageController
import pro.qyoga.core.clients.journals.dtos.EditJournalEntryRq
import pro.qyoga.tests.infra.web.pageRestTestClient
import pro.qyoga.tests.pages.therapist.clients.journal.entry.CreateJournalEntryForm
import pro.qyoga.tests.pages.therapist.clients.journal.entry.EditJournalEntryPage
import pro.qyoga.tests.platform.pathToRegex
import pro.qyoga.tests.platform.spring.rest_test_client.formData
import pro.qyoga.tests.platform.spring.rest_test_client.getBodyAsString
import java.time.LocalDate
import java.util.*


class TherapistClientJournalApi(
    override val authCookie: HttpCookie,
    private val restTestClient: RestTestClient = pageRestTestClient
) : AuthorizedApi {

    fun getCreateJournalEntryFragment(clientId: UUID, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return restTestClient.get()
            .uri(CreateJournalEntryPageController.CREATE_JOURNAL_PAGE_URL, clientId)
            .accept(MediaType.TEXT_HTML)
            .header("HX-Request", "true")
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(expectedStatus.value())
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun getCreateJournalEntryPage(clientId: UUID, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return restTestClient.get()
            .uri(CreateJournalEntryPageController.CREATE_JOURNAL_PAGE_URL, clientId)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(expectedStatus.value())
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun getEditJournalEntryPage(clientId: UUID, entryId: Long, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return restTestClient.get()
            .uri(EditJournalEntryPage.PATH, clientId, entryId)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(expectedStatus.value())
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun getJournalPage(clientId: UUID): Document {
        return restTestClient.get()
            .uri(JournalPageController.JOURNAL_PAGE_PATH, clientId)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun getJournalPagePage(clientId: UUID, after: LocalDate): Document {
        return restTestClient.get()
            .uri { builder ->
                builder.path(JournalPageController.JOURNAL_PAGE_PAGE_PATH).apply {
                    queryParam("after", after.toString())
                }.build(mapOf("clientId" to clientId))
            }
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun createJournalEntry(clientId: UUID, journalEntry: EditJournalEntryRq) {
        postNewJournalEntry(journalEntry, clientId).expectStatus().isNoContent
    }

    fun createJournalEntryForError(
        clientId: UUID,
        journalEntry: EditJournalEntryRq,
        expectedStatus: HttpStatus = HttpStatus.OK
    ): Document {
        return postNewJournalEntry(journalEntry, clientId)
            .expectStatus().isEqualTo(expectedStatus.value())
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    private fun postNewJournalEntry(
        journalEntry: EditJournalEntryRq,
        clientId: UUID
    ): RestTestClient.ResponseSpec {
        return restTestClient.post()
            .uri(CreateJournalEntryPageController.CREATE_JOURNAL_PAGE_URL, clientId)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formData(
                CreateJournalEntryForm.version.name to journalEntry.version,
                CreateJournalEntryForm.dateInput.name to journalEntry.date.toString(),
                CreateJournalEntryForm.therapeuticTaskComboBox.name to "",
                CreateJournalEntryForm.therapeuticTaskComboBox.titleInputId to journalEntry.therapeuticTaskTitle,
                CreateJournalEntryForm.entryTextInput.name to journalEntry.journalEntryText,
            ))
            .authorized()
            .exchange()
    }

    fun editJournalEntry(
        clientId: UUID,
        entryId: Long,
        journalEntry: EditJournalEntryRq,
    ) {
        postJournalEntryEdit(clientId, entryId, journalEntry)
            .expectStatus().isOk
            .expectHeader().valueMatches("Hx-Redirect", ".*" + JournalPageController.JOURNAL_PAGE_PATH.pathToRegex())
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun editJournalEntryForError(
        clientId: UUID,
        entryId: Long,
        journalEntry: EditJournalEntryRq,
        expectedStatus: HttpStatus = HttpStatus.OK
    ): Document {
        return postJournalEntryEdit(clientId, entryId, journalEntry)
            .expectStatus().isEqualTo(expectedStatus.value())
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    private fun postJournalEntryEdit(
        clientId: UUID,
        entryId: Long,
        journalEntry: EditJournalEntryRq,
    ): RestTestClient.ResponseSpec {
        return restTestClient.post()
            .uri(EditJournalEntryPage.PATH, clientId, entryId)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formData(
                CreateJournalEntryForm.version.name to journalEntry.version,
                CreateJournalEntryForm.dateInput.name to journalEntry.date.toString(),
                CreateJournalEntryForm.therapeuticTaskComboBox.name to "",
                CreateJournalEntryForm.therapeuticTaskComboBox.titleInputId to journalEntry.therapeuticTaskTitle,
                CreateJournalEntryForm.entryTextInput.name to journalEntry.journalEntryText,
            ))
            .authorized()
            .exchange()
    }

    fun deleteEntry(clientId: UUID, entryId: Long, expectedStatus: HttpStatus = HttpStatus.OK) {
        restTestClient.delete()
            .uri(EditJournalEntryPage.PATH, clientId, entryId)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(expectedStatus.value())
    }

}