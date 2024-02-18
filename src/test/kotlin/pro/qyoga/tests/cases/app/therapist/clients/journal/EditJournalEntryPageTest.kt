package pro.qyoga.tests.cases.app.therapist.clients.journal

import io.kotest.inspectors.forAny
import io.kotest.inspectors.forNone
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.l10n.russianDateFormat
import pro.qyoga.tests.assertions.*
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.pages.publc.GenericErrorPage
import pro.qyoga.tests.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.pages.therapist.clients.journal.entry.EditJournalEntryForm
import pro.qyoga.tests.pages.therapist.clients.journal.entry.EditJournalEntryPage
import pro.qyoga.tests.pages.therapist.clients.journal.entry.JournalEntryFrom
import pro.qyoga.tests.fixture.object_mothers.clients.JournalEntriesObjectMother.journalEntry
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import java.time.LocalDate


class EditJournalEntryPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Edit journal entry page should be rendered correctly`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()
        val createJournalEntryRequest = journalEntry()
        val entry =
            backgrounds.clientJournal.createJournalEntry(client.id, createJournalEntryRequest, theTherapistUserDetails)

        // When
        val document = therapist.clientJournal.getEditJournalEntryPage(client.id, entry.id)

        // Then
        document shouldBeElement EditJournalEntryPage.pageFor(entry)
    }

    @Test
    fun `Journal entry editing should be persistent`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()
        val createJournalEntryRequest = journalEntry()
        val entry =
            backgrounds.clientJournal.createJournalEntry(client.id, createJournalEntryRequest, theTherapistUserDetails)
        val editedEntry = journalEntry()

        // When
        therapist.clientJournal.editJournalEntry(client.id, entry.id, editedEntry)

        // Then
        val journal = backgrounds.clientJournal.getWholeJournal(client.id).content
        journal.forNone { it shouldBe entry }
        journal.forAny { it shouldMatch editedEntry }
    }

    @Test
    fun `When user sets date of journal entry to existing, prefilled form with validation error should be returned`() {
        // Given
        val firstEntryDate = LocalDate.now().minusDays(1)
        val secondEntryDate = LocalDate.now()
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()
        backgrounds.clientJournal.createJournalEntry(
            client.id,
            journalEntry(date = firstEntryDate),
            theTherapistUserDetails
        )
        val editJournalEntryRequest = journalEntry(date = secondEntryDate)
        val editedEntry =
            backgrounds.clientJournal.createJournalEntry(client.id, editJournalEntryRequest, theTherapistUserDetails)

        // When
        val document = therapist.clientJournal.editJournalEntryForError(
            client.id,
            editedEntry.id,
            editJournalEntryRequest.copy(date = firstEntryDate)
        )

        // Then
        document.select("body form").single() shouldBeComponent EditJournalEntryForm
        EditJournalEntryForm.dateInput.value(document) shouldBe russianDateFormat.format(firstEntryDate)
        EditJournalEntryForm.therapeuticTaskNameInput.value(document) shouldBe editJournalEntryRequest.therapeuticTaskName
        EditJournalEntryForm.entryTextInput.value(document) shouldBe editJournalEntryRequest.journalEntryText
        document shouldHave JournalEntryFrom.DUPLICATED_DATE_MESSAGE
    }

    @Test
    fun `Post of edit journal entry request for not existing entry id should return generic error page`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val notExistingClientId: Long = -1
        val notExistingEntryId: Long = -1
        val anyJournalEntry = journalEntry()

        // When
        val document = therapist.clientJournal.editJournalEntryForError(
            notExistingClientId,
            notExistingEntryId,
            anyJournalEntry,
            expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR
        )

        // Then
        document shouldBe GenericErrorPage
    }

    @Test
    fun `When edit page of not existing entry is requested, generic 404 error page should be returned`() {
        // Given
        val client = backgrounds.clients.createClients(1).single()
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clientJournal.getEditJournalEntryPage(client.id, -1, HttpStatus.NOT_FOUND)

        // Then
        document shouldBePage NotFoundErrorPage
    }

}