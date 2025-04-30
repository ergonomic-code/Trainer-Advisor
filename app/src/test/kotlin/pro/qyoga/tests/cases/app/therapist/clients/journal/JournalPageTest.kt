package pro.qyoga.tests.cases.app.therapist.clients.journal

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.core.clients.journals.dtos.JournalPageRq
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother.createClientCardDto
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.fixture.presets.ClientsFixturePresets
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.pages.therapist.clients.journal.list.ClientJournalEntriesFragment
import pro.qyoga.tests.pages.therapist.clients.journal.list.EmptyClientJournalPage
import pro.qyoga.tests.pages.therapist.clients.journal.list.NonEmptyClientJournalPage

@DisplayName("Страница журнала клиента")
class JournalPageTest : QYogaAppIntegrationBaseTest() {

    private val clientsFixturePresets = getBean<ClientsFixturePresets>()

    @Test
    fun `должна отображать 10 записей, если они есть`() {
        // Сетап
        val pageSize = 10
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()
        var allEntries = backgrounds.clientJournal.createEntries(client.id, theTherapistUserDetails, pageSize + 1)
        allEntries = backgrounds.clientJournal.hydrate(allEntries)
        val firstPageEntries = allEntries.sortedByDescending { it.date }.take(pageSize)

        // Действие
        val document = therapist.clientJournal.getJournalPage(client.id)

        // Проверка
        document shouldBe NonEmptyClientJournalPage(client.id, firstPageEntries, hasMore = true)
    }

    @Test
    fun `должна удалять записи`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()
        val entry = backgrounds.clientJournal.createEntries(client.id, theTherapistUserDetails, 1).single()

        // Действие
        therapist.clientJournal.deleteEntry(client.id, entry.id, HttpStatus.OK)
        val document = therapist.clientJournal.getJournalPage(client.id)

        // Проверка
        document shouldBe EmptyClientJournalPage(client.id)
    }

    @Test
    fun `должна возвращать 200 статус при удалении несуществующей записи`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val notExistingClientId = ClientsObjectMother.randomId()
        val notExistingEntryId = -1L

        // Действие/Проверка
        therapist.clientJournal.deleteEntry(
            notExistingClientId,
            notExistingEntryId,
            expectedStatus = HttpStatus.OK
        )
    }

    @Test
    fun `должна возвращать страницу ошибки 404 при запросе для несуществующего клиента`() {
        // Сетап
        val notExistingClientId = ClientsObjectMother.randomId()
        val therapist = TherapistClient.loginAsTheTherapist()

        // Действие
        val document = therapist.clients.getClientEditPage(notExistingClientId, expectedStatus = HttpStatus.NOT_FOUND)

        // Проверка
        document shouldBePage NotFoundErrorPage
    }

    @Test
    fun `должна рендерится корректно при отсутствии записей`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val newClientRequest = createClientCardDto()
        val client = backgrounds.clients.createClients(listOf(newClientRequest), THE_THERAPIST_ID).first()

        // Действие
        val document = therapist.clients.getClientEditPage(client.id)

        // Проверка
        document shouldBe EmptyClientJournalPage(client.id)
    }

    @Test
    fun `должна не содержать лоадер следующей страницы, если кол-во записей в БД меньше размера страницы`() {
        // Сетап
        val (client, journal) = clientsFixturePresets.createAClientWithJournalEntry()

        // Действие
        val document = theTherapist.clientJournal.getJournalPage(client.id)

        // Проверка
        document shouldBe NonEmptyClientJournalPage(client.id, journal, hasMore = false)
    }

    @Test
    fun `при запросе второй страницы должна содержать лоадер, если кол-во записей в БД больше размера двух страниц`() {
        // Сетап
        val (client, journal) = clientsFixturePresets.createAClientWithJournalEntries(journalEntriesCount = JournalPageRq.DEFAULT_PAGE_SIZE * 2 + 1)
        val firstPage = journal.sortedByDescending(JournalEntry::date).take(JournalPageRq.DEFAULT_PAGE_SIZE)
        val secondPage = journal.sortedByDescending(JournalEntry::date).drop(JournalPageRq.DEFAULT_PAGE_SIZE)
            .take(JournalPageRq.DEFAULT_PAGE_SIZE)

        // Действие
        val document = theTherapist.clientJournal.getJournalPagePage(client.id, firstPage.last().date)

        // Проверка
        document shouldBe ClientJournalEntriesFragment.fragmentFor(secondPage, hasMore = true)
    }

    @Test
    fun `при запросе второй страницы должна не содержать лоадер, если кол-во записей в БД равно размеру двух страниц`() {
        // Сетап
        val (client, journal) = clientsFixturePresets.createAClientWithJournalEntries(journalEntriesCount = JournalPageRq.DEFAULT_PAGE_SIZE * 2)
        val firstPage = journal.sortedByDescending(JournalEntry::date).take(JournalPageRq.DEFAULT_PAGE_SIZE)
        val secondPage = journal.sortedByDescending(JournalEntry::date).drop(JournalPageRq.DEFAULT_PAGE_SIZE)
            .take(JournalPageRq.DEFAULT_PAGE_SIZE)

        // Действие
        val document = theTherapist.clientJournal.getJournalPagePage(client.id, firstPage.last().date)

        // Проверка
        document shouldBe ClientJournalEntriesFragment.fragmentFor(secondPage, hasMore = false)
    }

}