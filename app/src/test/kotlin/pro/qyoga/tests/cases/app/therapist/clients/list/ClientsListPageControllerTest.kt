package pro.qyoga.tests.cases.app.therapist.clients.list

import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldBeSortedWith
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.app.therapist.clients.list.ClientListItemView
import pro.qyoga.app.therapist.clients.list.ClientsListPageController
import pro.qyoga.core.clients.cards.dtos.ClientSearchDto
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother.createClientCardDto
import pro.qyoga.tests.fixture.object_mothers.clients.JournalEntriesObjectMother.journalEntry
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


@DisplayName("Контроллер страницы списка клиентов")
class ClientsListPageControllerTest : QYogaAppIntegrationBaseTest() {

    private val clientsListPageController = getBean<ClientsListPageController>()

    @Test
    fun `должен возвращать список клиентов, отсортированный по убыванию даты 'касания'`() {
        // Сетап
        val clientsCount = 10

        backgrounds.clients.createClients(clientsCount)

        // Действие
        val clients = clientsListPageController.getClients(
            theTherapistUserDetails,
            PageRequest.ofSize(clientsCount),
        )
            .clients

        // Проверка
        clients shouldHaveSize clientsCount
        clients shouldBeSortedWith Comparator.comparing(ClientListItemView::createdAt).reversed()
    }

    @Test
    fun `при запросе без фильтрации должен возвращать только клиентов терапевта`() {
        // Сетап
        val ownClientsCount = 5
        val alienClientsCount = 5

        val anotherTherapist = backgrounds.users.registerNewTherapist()

        backgrounds.clients.createClients(ownClientsCount, THE_THERAPIST_ID)
        backgrounds.clients.createClients(alienClientsCount, anotherTherapist.id)

        // Действие
        val clients = clientsListPageController.getClients(
            theTherapistUserDetails,
            PageRequest.ofSize(Int.MAX_VALUE),
        )
            .clients

        // Проверка
        clients.content shouldHaveSize ownClientsCount
        clients.content.forAll { it.therapistRef shouldBe THE_THERAPIST_REF }
    }

    @Test
    fun `при запросе с фильтрацией должен возвращать только клиентов терапевта`() {
        // Сетап
        val ownClientsCount = 5
        val alienClientsCount = 5

        val anotherTherapist = backgrounds.users.registerNewTherapist()

        backgrounds.clients.createClients(ownClientsCount, THE_THERAPIST_ID)
        backgrounds.clients.createClients(alienClientsCount, anotherTherapist.id)


        // Действие
        val clients = clientsListPageController.getClientsFiltered(
            theTherapistUserDetails,
            ClientSearchDto.ALL,
            PageRequest.ofSize(Int.MAX_VALUE),
        )
            .clients

        // Проверка
        clients.content shouldHaveSize ownClientsCount
        clients.content.forAll { it.therapistRef shouldBe THE_THERAPIST_REF }
    }

    @Test
    fun `должен учитывать дату изменения карточки клиента при сортировке`() {
        // Сетап

        val (_, createdLaterClient) = backgrounds.clients.createClients(2).toList()
        backgrounds.clients.updateClient(createdLaterClient.id, createClientCardDto())

        // Проверка
        val clients = clientsListPageController.getClients(theTherapistUserDetails, Pageable.ofSize(10))
            .clients

        // Действие
        clients.content[0].id shouldBe createdLaterClient.id
    }

    @Test
    fun `должен учитывать дату создания последней записи журнала клиента при сортировке`() {
        // Сетап

        val (_, createdLaterClient) = backgrounds.clients.createClients(2).toList()
        backgrounds.clientJournal.createJournalEntry(createdLaterClient.id, journalEntry(), theTherapistUserDetails)

        // Проверка
        val clients = clientsListPageController.getClients(theTherapistUserDetails, Pageable.ofSize(10))
            .clients

        // Действие
        clients.content[0].id shouldBe createdLaterClient.id
    }

    @Test
    fun `должен учитывать дату последней модификации записи журнала клиента при сортировке`() {
        // Сетап

        val (_, createdLaterClient) = presets.clientsFixturePresets.createAClientsWithJournalEntry(clientsCount = 2)
        backgrounds.clientJournal.updateJournalEntry(
            createdLaterClient.client.ref(),
            createdLaterClient.journal.single().id,
            journalEntry(),
            theTherapistUserDetails
        )

        // Проверка
        val clients = clientsListPageController.getClients(theTherapistUserDetails, Pageable.ofSize(10))
            .clients

        // Действие
        clients.content[0].id shouldBe createdLaterClient.client.id
    }

}