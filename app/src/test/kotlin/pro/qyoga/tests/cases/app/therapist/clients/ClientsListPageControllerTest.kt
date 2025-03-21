package pro.qyoga.tests.cases.app.therapist.clients

import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldBeSortedWith
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import pro.qyoga.app.therapist.clients.ClientsListPageController
import pro.qyoga.core.clients.cards.dtos.ClientSearchDto
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
        clients shouldBeSortedWith Comparator.comparing { it.lastName.lowercase() }
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

}