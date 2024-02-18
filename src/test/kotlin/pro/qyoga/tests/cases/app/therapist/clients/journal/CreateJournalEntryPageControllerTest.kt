package pro.qyoga.tests.cases.app.therapist.clients.journal

import io.kotest.inspectors.forAny
import org.junit.jupiter.api.Test
import pro.qyoga.app.therapist.clients.journal.edit_entry.create.CreateJournalEntryPageController
import pro.qyoga.app.therapist.clients.journal.list.JournalPageController
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.fixture.object_mothers.clients.JournalEntriesObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import java.time.LocalDate


class CreateJournalEntryPageControllerTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Journal entry for today should be included in first journal page`() {
        // Given
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()
        val createJournalEntryRequest = JournalEntriesObjectMother.journalEntry(date = LocalDate.now())

        // When
        backgrounds.clientJournal.createJournalEntry(client.id, createJournalEntryRequest, theTherapistUserDetails)

        // And when
        val modelAndView = getBean<JournalPageController>().getJournalPage(client.id)

        // Then
        val journal = JournalPageController.getJournal(modelAndView.model).content
        journal.forAny { it shouldMatch createJournalEntryRequest }
    }

    @Test
    fun `Journal entry page should accept existing therapeutic task name and reuse existing entity`() {
        // Given
        val taskName = "Йогатерапия гастрита"
        backgrounds.therapeuticTasks.createTherapeuticTask(THE_THERAPIST_ID, taskName)
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()
        val createJournalEntryRequest =
            JournalEntriesObjectMother.journalEntry(date = LocalDate.now(), therapeuticTaskName = taskName)

        // When
        getBean<CreateJournalEntryPageController>().createJournalEntry(
            client.id,
            createJournalEntryRequest,
            theTherapistUserDetails
        )

        // Then
        backgrounds.clientJournal.getWholeJournal(client.id).content.forAny { it shouldMatch createJournalEntryRequest }
    }

}