package pro.qyoga.tests.cases.app.therapist.clients.journal

import io.kotest.inspectors.forAny
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import pro.qyoga.app.therapist.clients.journal.JournalPageController
import pro.qyoga.core.formats.russianDateFormat
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.therapist.clients.journal.CreateJournalEntryPage
import pro.qyoga.tests.fixture.clients.JournalEntriesObjectMother
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_ID
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

}