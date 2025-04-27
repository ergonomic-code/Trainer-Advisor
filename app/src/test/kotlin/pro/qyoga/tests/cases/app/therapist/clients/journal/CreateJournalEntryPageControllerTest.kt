package pro.qyoga.tests.cases.app.therapist.clients.journal

import io.kotest.inspectors.forAny
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.qyoga.app.therapist.clients.journal.edit_entry.create.CreateJournalEntryPageController
import pro.qyoga.app.therapist.clients.journal.list.JournalPageController
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.fixture.object_mothers.clients.JournalEntriesObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import java.time.LocalDate


@DisplayName("Страница создания записи в журнале")
class CreateJournalEntryPageControllerTest : QYogaAppIntegrationBaseTest() {

    private val journalPageController = getBean<JournalPageController>()

    @DisplayName("После создания запииси в журнале, она должно отображаться на первой странице журнала")
    @Test
    fun creationOfJournalEntry() {
        // Given
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()
        val createJournalEntryRequest = JournalEntriesObjectMother.journalEntry(date = LocalDate.now())

        // When
        backgrounds.clientJournal.createJournalEntry(client.id, createJournalEntryRequest, theTherapistUserDetails)

        // And when
        val journal = journalPageController.handleGetJournalFragment(client.id).fragmentModel
            .page
            .content

        // Then
        journal.forAny { it shouldMatch createJournalEntryRequest }
    }

    @DisplayName("При добавлении записи в журнал с указанием названия существующей терапевтической задачи, запись должна успешно создаваться")
    @Test
    fun createJournalEntryWithExistingTherapeuticTask() {
        // Given
        val taskName = "Йогатерапия гастрита"
        backgrounds.therapeuticTasks.createTherapeuticTask(THE_THERAPIST_ID, taskName)
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()
        val createJournalEntryRequest =
            JournalEntriesObjectMother.journalEntry(date = LocalDate.now(), therapeuticTaskName = taskName)

        // When
        getBean<CreateJournalEntryPageController>().handleCreateJournalEntry(
            client.id,
            createJournalEntryRequest,
            theTherapistUserDetails
        )

        // Then
        backgrounds.clientJournal.getWholeJournal(client.id).content.forAny { it shouldMatch createJournalEntryRequest }
    }

    @DisplayName(
        "При создании записи в журнале с терапевтической задачей, не существующей у данного терапевта, но существующей у другого терапевта" +
                "должна быть создана новая терапветическая задача данного терапевта"
    )
    @Test
    fun createJournalEntryWithTaskExistingForAnotherTherapist() {
        // Given
        val taskName = "Йогатерапия гастрита"
        val anotherTherapist = backgrounds.users.registerNewTherapist()
        val anotherTherapeuticTask = backgrounds.therapeuticTasks.createTherapeuticTask(anotherTherapist.id, taskName)
        val client = backgrounds.clients.aClient()

        val createJournalEntryRequest =
            JournalEntriesObjectMother.journalEntry(therapeuticTaskName = taskName)

        // When
        getBean<CreateJournalEntryPageController>().handleCreateJournalEntry(
            client.id,
            createJournalEntryRequest,
            theTherapistUserDetails
        )

        // Then
        val journal = backgrounds.clientJournal.getWholeJournal(client.id).content
        journal.forAny {
            it shouldMatch createJournalEntryRequest
            it.therapeuticTask?.id shouldNotBe anotherTherapeuticTask.id
        }
    }


}