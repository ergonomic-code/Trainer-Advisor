package pro.qyoga.tests.cases.app.therapist.clients.journal

import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.therapist.clients.journal.NonEmptyClientJournalPage
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.therapists.theTherapistUserDetails
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


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
        document shouldBe NonEmptyClientJournalPage(firstPageEntries)
    }

}