package pro.qyoga.tests.cases.app.therapist.clients.journal

import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.therapist.clients.journal.list.EmptyClientJournalPage
import pro.qyoga.tests.pages.therapist.clients.journal.list.NonEmptyClientJournalPage


class JournalPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Client journal page should contain 10 entries if they are in db`() {
        // Given
        val pageSize = 10
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()
        var allEntries = backgrounds.clientJournal.createEntries(client.id, theTherapistUserDetails, pageSize + 1)
        allEntries = backgrounds.clientJournal.hydrate(allEntries)
        val firstPageEntries = allEntries.sortedByDescending { it.date }.take(pageSize)

        // When
        val document = therapist.clientJournal.getJournalPage(client.id)

        // Then
        document shouldBe NonEmptyClientJournalPage(client.id, firstPageEntries)
    }

    @Test
    fun `Journal entry should disappear after deletion`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()
        val entry = backgrounds.clientJournal.createEntries(client.id, theTherapistUserDetails, 1).single()

        // When
        therapist.clientJournal.deleteEntry(client.id, entry.id, HttpStatus.OK)
        val document = therapist.clientJournal.getJournalPage(client.id)

        // Then
        document shouldBe EmptyClientJournalPage(client.id)
    }

    @Test
    fun `When not existing journal entry is deleted response with 200 status should be returned`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val notExistingClientId = ClientsObjectMother.randomId()
        val notExistingEntryId = -1L

        // When/Then
        therapist.clientJournal.deleteEntry(
            notExistingClientId,
            notExistingEntryId,
            expectedStatus = HttpStatus.OK
        )
    }

}