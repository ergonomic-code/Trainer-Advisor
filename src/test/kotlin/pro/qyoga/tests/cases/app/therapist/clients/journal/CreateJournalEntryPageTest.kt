package pro.qyoga.tests.cases.app.therapist.clients.journal

import io.kotest.inspectors.forAny
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.app.therapist.clients.journal.list.JournalPageController
import pro.qyoga.l10n.russianDateFormat
import pro.qyoga.tests.assertions.*
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.pages.publc.GenericErrorPage
import pro.qyoga.tests.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.pages.therapist.clients.journal.entry.CreateJournalEntryForm
import pro.qyoga.tests.pages.therapist.clients.journal.entry.CreateJournalEntryPage
import pro.qyoga.tests.pages.therapist.clients.journal.entry.JournalEntryFrom
import pro.qyoga.tests.fixture.object_mothers.clients.JournalEntriesObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import java.time.LocalDate


class CreateJournalEntryPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Create journal entry page should be rendered correctly`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()

        // When
        val document = therapist.clientJournal.getCreateJournalEntryPage(client.id)

        // Then
        document shouldHaveComponent CreateJournalEntryPage
    }

    @Test
    fun `Created journal entry should be added to journal page`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()
        val createJournalEntryRequest = JournalEntriesObjectMother.journalEntry()

        // When
        therapist.clientJournal.createJournalEntry(client.id, createJournalEntryRequest)

        // And when
        val modelAndView = getBean<JournalPageController>().getJournalPage(client.id)

        // Then
        val journal = JournalPageController.getJournal(modelAndView.model).content
        journal.forAny { it shouldMatch createJournalEntryRequest }
    }

    @Test
    fun `Entry date field on create page should be prefilled with current date`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()

        // When
        val document = therapist.clientJournal.getCreateJournalEntryPage(client.id)

        // Then
        CreateJournalEntryForm.dateInput.value(document) shouldBe russianDateFormat.format(LocalDate.now())
    }

    @Test
    fun `When user creates duplicated journal entry for the date prefilled form with validation error should be returned`() {
        // Given
        val entryDate = LocalDate.now()
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()
        val createJournalEntryRequest = JournalEntriesObjectMother.journalEntry(date = entryDate)
        backgrounds.clientJournal.createJournalEntry(client.id, createJournalEntryRequest, theTherapistUserDetails)

        // When
        val document = therapist.clientJournal.createJournalEntryForError(client.id, createJournalEntryRequest)

        // Then
        document.select("body form").single() shouldBeComponent CreateJournalEntryForm
        CreateJournalEntryForm.dateInput.value(document) shouldBe russianDateFormat.format(LocalDate.now())
        CreateJournalEntryForm.therapeuticTaskNameInput.value(document) shouldBe createJournalEntryRequest.therapeuticTaskName
        CreateJournalEntryForm.entryTextInput.value(document) shouldBe createJournalEntryRequest.journalEntryText
        document shouldHave JournalEntryFrom.DUPLICATED_DATE_MESSAGE
    }

    @Test
    fun `Request of create journal entry page for not existing client id should return 404 error page`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val notExistingClientId: Long = -1

        // When
        val document = therapist.clientJournal.getCreateJournalEntryPage(
            notExistingClientId,
            expectedStatus = HttpStatus.NOT_FOUND
        )

        // Then
        document shouldBePage NotFoundErrorPage
    }

    @Test
    fun `Post of create journal entry request for not existing client id should return generic error page`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val notExistingClientId: Long = -1
        val anyJournalEntry = JournalEntriesObjectMother.journalEntry()

        // When
        val document = therapist.clientJournal.createJournalEntryForError(
            notExistingClientId,
            anyJournalEntry,
            expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR
        )

        // Then
        document shouldBe GenericErrorPage
    }

}