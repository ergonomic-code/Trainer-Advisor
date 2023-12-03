package pro.qyoga.fixture.backgrounds

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.ui.ExtendedModelMap
import pro.qyoga.app.therapist.clients.ClientListPageController
import pro.qyoga.core.clients.api.ClientCardDto
import pro.qyoga.core.clients.api.ClientsService
import pro.qyoga.core.clients.internal.Client
import pro.qyoga.fixture.clients.ClientsObjectMother
import pro.qyoga.fixture.therapists.THE_THERAPIST_ID
import pro.qyoga.fixture.therapists.theTherapistUserDetails

@Component
class ClientsBackgrounds(
    private val clientsService: ClientsService,
    private val clientListPageController: ClientListPageController
) {

    fun createClients(clients: List<ClientCardDto>, therapistId: Long = THE_THERAPIST_ID): Iterable<Client> {
        return clientsService.createClients(therapistId, clients)
    }

    fun createClients(count: Int, therapistId: Long = THE_THERAPIST_ID) {
        clientsService.createClients(therapistId, ClientsObjectMother.createClientCardDtos(count))
    }

    fun getAllClients(): Page<Client> {
        val model = ExtendedModelMap()
        clientListPageController.getClients(theTherapistUserDetails, Pageable.ofSize(Int.MAX_VALUE), model)
        return ClientListPageController.getClients(model)
    }

}