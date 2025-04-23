package pro.qyoga.tests.cases.app.therapist.clients.journal

import io.kotest.inspectors.forAny
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.app.therapist.clients.journal.list.JournalPageController
import pro.qyoga.l10n.russianDateFormat
import pro.qyoga.tests.assertions.shouldBeComponent
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.assertions.shouldHaveElement
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.clients.JournalEntriesObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.publc.GenericErrorPage
import pro.qyoga.tests.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.pages.therapist.clients.journal.entry.CreateJournalEntryForm
import pro.qyoga.tests.pages.therapist.clients.journal.entry.CreateJournalEntryFragment
import pro.qyoga.tests.pages.therapist.clients.journal.entry.JournalEntryFrom
import java.time.LocalDate


@DisplayName("Фрагмент создания записи журнала страницы клиента")
class CreateJournalEntryFragmentTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `должен рендерится корректно`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()

        // Действие
        val document = therapist.clientJournal.getCreateJournalEntryFragment(client.id)

        // Проверка
        document shouldBePage CreateJournalEntryFragment(client.id, LocalDate.now())
    }

    @Test
    fun `должен добавлять запись в журнал`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()
        val createJournalEntryRequest = JournalEntriesObjectMother.journalEntry()

        // Действие
        therapist.clientJournal.createJournalEntry(client.id, createJournalEntryRequest)

        // И действие
        val modelAndView = getBean<JournalPageController>().handleGetJournalPage(client.id)

        // Проверка
        val journal = JournalPageController.getJournal(modelAndView.model).content
        journal.forAny { it shouldMatch createJournalEntryRequest }
    }

    @Test
    fun `должа отображать ошибку валидации, при попытки создать запись за дату, для которой уже есть запись`() {
        // Сетап
        val entryDate = LocalDate.now()
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()
        val createJournalEntryRequest = JournalEntriesObjectMother.journalEntry(date = entryDate)
        backgrounds.clientJournal.createJournalEntry(client.id, createJournalEntryRequest, theTherapistUserDetails)

        // Действие
        val document = therapist.clientJournal.createJournalEntryForError(client.id, createJournalEntryRequest)

        // Проверка
        document.select("body form").single() shouldBeComponent CreateJournalEntryForm
        CreateJournalEntryForm.dateInput.value(document) shouldBe russianDateFormat.format(LocalDate.now())
        CreateJournalEntryForm.therapeuticTaskNameInput.value(document) shouldBe createJournalEntryRequest.therapeuticTaskName
        CreateJournalEntryForm.entryTextInput.value(document) shouldBe createJournalEntryRequest.journalEntryText
        document shouldHaveElement JournalEntryFrom.DUPLICATED_DATE_MESSAGE
    }

    @Test
    fun `должна отображтаь страницу ошибки 404 при запросе страницы создания записи журнала для несуществующего клиента`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val notExistingClientId = ClientsObjectMother.randomId()

        // Действие
        val document = therapist.clientJournal.getCreateJournalEntryFragment(
            notExistingClientId,
            expectedStatus = HttpStatus.NOT_FOUND
        )

        // Проверка
        document shouldBePage NotFoundErrorPage
    }

    @Test
    fun `должна отображтаь страницу ошибки 500 при запросе создания записи журнала для несуществующего клиента`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val notExistingClientId = ClientsObjectMother.randomId()
        val anyJournalEntry = JournalEntriesObjectMother.journalEntry()

        // Действие
        val document = therapist.clientJournal.createJournalEntryForError(
            notExistingClientId,
            anyJournalEntry,
            expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR
        )

        // Проверка
        document shouldBePage GenericErrorPage
    }

}