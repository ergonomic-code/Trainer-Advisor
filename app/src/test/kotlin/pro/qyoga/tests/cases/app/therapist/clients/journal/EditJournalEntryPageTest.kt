package pro.qyoga.tests.cases.app.therapist.clients.journal

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.l10n.russianDateFormat
import pro.qyoga.tests.assertions.*
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.clients.JournalEntriesObjectMother.journalEntry
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.fixture.presets.ClientsFixturePresets
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.publc.GenericErrorPage
import pro.qyoga.tests.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.pages.therapist.clients.journal.entry.EditJournalEntryForm
import pro.qyoga.tests.pages.therapist.clients.journal.entry.EditJournalEntryPage
import pro.qyoga.tests.pages.therapist.clients.journal.entry.JournalEntryFrom
import java.time.LocalDate


@DisplayName("Странциа редактирования записи журнала")
class EditJournalEntryPageTest : QYogaAppIntegrationBaseTest() {

    private val clientFixturePresets = getBean<ClientsFixturePresets>()

    @Test
    fun `должна рендериться корректно`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()
        val createJournalEntryRequest = journalEntry()
        val entry =
            backgrounds.clientJournal.createJournalEntry(client.id, createJournalEntryRequest, theTherapistUserDetails)

        // Действие
        val document = therapist.clientJournal.getEditJournalEntryPage(client.id, entry.id)

        // Проверка
        document shouldBeElement EditJournalEntryPage.pageFor(entry)
    }

    @Test
    fun `должна сохранять изменения`() {
        // Сетап
        val (client, entries) = clientFixturePresets.createAClientWithJournalEntry()
        val entry = entries.single()
        val editEntryRq = journalEntry()

        // Действие
        theTherapist.clientJournal.editJournalEntry(client.id, entry.id, editEntryRq)

        // Проверка
        val updatedEntry = backgrounds.clientJournal.getWholeJournal(client.id).content.single()
        updatedEntry shouldMatch editEntryRq
        updatedEntry.lastModifiedAt shouldNotBe null
    }

    @Test
    fun `должна отображать ошибку валидации при попытки установить дату записи в дату, для которой уже существует другая запись`() {
        // Сетап
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

        // Действие
        val document = therapist.clientJournal.editJournalEntryForError(
            client.id,
            editedEntry.id,
            editJournalEntryRequest.copy(date = firstEntryDate)
        )

        // Проверка
        document.select("body form").single() shouldBeComponent EditJournalEntryForm
        EditJournalEntryForm.dateInput.value(document) shouldBe russianDateFormat.format(firstEntryDate)
        EditJournalEntryForm.therapeuticTaskNameInput.value(document) shouldBe editJournalEntryRequest.therapeuticTaskName
        EditJournalEntryForm.entryTextInput.value(document) shouldBe editJournalEntryRequest.journalEntryText
        document shouldHaveElement JournalEntryFrom.DUPLICATED_DATE_MESSAGE
    }

    @Test
    fun `должна отображать страницу ошибки 500 при попытки отредактировать несуществующую запись`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val notExistingClientId = ClientsObjectMother.randomId()
        val notExistingEntryId: Long = -1
        val anyJournalEntry = journalEntry()

        // Действие
        val document = therapist.clientJournal.editJournalEntryForError(
            notExistingClientId,
            notExistingEntryId,
            anyJournalEntry,
            expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR
        )

        // Проверка
        document shouldBePage GenericErrorPage
    }

    @Test
    fun `должна отображать страницу ошибки 404 при попытки отредактировать запись журнала несуществующего клиента`() {
        // Сетап
        val client = backgrounds.clients.createClients(1).single()
        val therapist = TherapistClient.loginAsTheTherapist()

        // Действие
        val document = therapist.clientJournal.getEditJournalEntryPage(client.id, -1, HttpStatus.NOT_FOUND)

        // Проверка
        document shouldBePage NotFoundErrorPage
    }

}