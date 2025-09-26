package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import pro.qyoga.app.therapist.clients.cards.CreateClientCardPageController
import pro.qyoga.app.therapist.clients.cards.EditClientCardPageController
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
    private val journalBackgrounds: ClientJournalBackgrounds,
    private val createClientPageController: CreateClientCardPageController,
    private val editClientPageController: EditClientCardPageController
) {

    fun aClient(): Client {
        return createClients(1, THE_THERAPIST_ID).single()
    }

    fun createClients(clients: Collection<ClientCardDto>, therapistId: UUID = THE_THERAPIST_ID): Iterable<Client> {
        return clientsRepo.saveAll(clients.map { ClientsObjectMother.createClient(therapistId, it) })
    }

    fun createClients(count: Int, therapistId: UUID = THE_THERAPIST_ID): Iterable<Client> {
        return createClients(ClientsObjectMother.createClientCardDtos(count), therapistId)
    }

    fun getAllClients(): List<Client> {
        return clientsRepo.findAll()
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
        createClientPageController.createClient(clientDto, principal)
        return clientsRepo.findByPhone(principal.ref, PhoneNumber.of(clientDto.phoneNumber))!!
    }

    fun updateClient(clientId: UUID, editClientCardDto: ClientCardDto) {
        editClientPageController.editClientCard(editClientCardDto, clientId)
    }

}