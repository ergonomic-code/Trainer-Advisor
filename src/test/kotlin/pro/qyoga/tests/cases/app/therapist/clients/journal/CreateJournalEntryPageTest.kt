package pro.qyoga.tests.cases.app.therapist.clients.journal

import io.kotest.inspectors.forAny
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.app.therapist.clients.journal.JournalPageController
import pro.qyoga.core.formats.russianDateFormat
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldBeElement
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.publc.GenericErrorPage
import pro.qyoga.tests.clients.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.clients.pages.therapist.clients.journal.CreateJournalEntryPage
import pro.qyoga.tests.fixture.clients.JournalEntriesObjectMother
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.therapists.theTherapistUserDetails
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
        document shouldBe CreateJournalEntryPage
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
        CreateJournalEntryPage.JournalEntryFrom.dateInput.value(document) shouldBe russianDateFormat.format(LocalDate.now())
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
        document.select("body form").single() shouldBeElement CreateJournalEntryPage.JournalEntryFrom
        CreateJournalEntryPage.JournalEntryFrom.dateInput.value(document) shouldBe russianDateFormat.format(LocalDate.now())
        CreateJournalEntryPage.JournalEntryFrom.therapeuticTaskNameInput.value(document) shouldBe createJournalEntryRequest.therapeuticTaskName
        CreateJournalEntryPage.JournalEntryFrom.entryTextInput.value(document) shouldBe createJournalEntryRequest.journalEntryText
        document shouldHave CreateJournalEntryPage.JournalEntryFrom.DUPLICATED_DATE_MESSAGE
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
        document shouldBe NotFoundErrorPage
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