package pro.qyoga.tests.fixture.backgrounds

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.ui.ExtendedModelMap
import pro.qyoga.app.therapist.clients.ClientsListPageController
import pro.qyoga.app.therapist.clients.cards.CreateClientCardPageController
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.findByPhone
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.model.PhoneNumber
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapists.idOnlyUserDetails
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import java.util.*

@Component
class ClientsBackgrounds(
    private val clientsRepo: ClientsRepo,
    private val clientsListPageController: ClientsListPageController,
    private val journalBackgrounds: ClientJournalBackgrounds,
    private val clientsPageController: CreateClientCardPageController
) {

    fun aClient(): Client {
        return createClients(1, THE_THERAPIST_ID).single()
    }

    fun createClients(clients: List<ClientCardDto>, therapistId: UUID = THE_THERAPIST_ID): Iterable<Client> {
        return clientsRepo.saveAll(clients.map { ClientsObjectMother.createClient(therapistId, it) })
    }

    fun createClients(count: Int, therapistId: UUID = THE_THERAPIST_ID): Iterable<Client> {
        return createClients(ClientsObjectMother.createClientCardDtos(count), therapistId)
    }

    fun getAllClients(): List<Client> {
        val model = ExtendedModelMap()
        clientsListPageController.getClients(theTherapistUserDetails, Pageable.ofSize(Int.MAX_VALUE), model)
        return ClientsListPageController.getClients(model).content
    }

    fun createClientWithJournalEntry(therapistId: UUID = THE_THERAPIST_ID): Pair<Client, JournalEntry> {
        val client = createClients(1, therapistId).single()
        val entry = journalBackgrounds.createEntries(client.id, idOnlyUserDetails(therapistId), 1).single()
        return client to entry
    }

    fun createClient(
        phone: String,
        therapistId: UUID = THE_THERAPIST_ID
    ): Client {
        return createClients(listOf(ClientsObjectMother.createClientCardDto(phone = phone)), therapistId).single()
    }

    fun createClient(
        clientDto: ClientCardDto = ClientsObjectMother.createClientCardDtoMinimal(),
        principal: QyogaUserDetails = theTherapistUserDetails
    ): Client {
        clientsPageController.createClient(clientDto, principal)
        return clientsRepo.findByPhone(principal.ref, PhoneNumber.of(clientDto.phoneNumber))!!
    }

}