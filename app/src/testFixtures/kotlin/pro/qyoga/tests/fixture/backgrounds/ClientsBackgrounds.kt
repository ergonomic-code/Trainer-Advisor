package pro.qyoga.tests.fixture.backgrounds

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.ui.ExtendedModelMap
import pro.qyoga.app.therapist.clients.ClientsListPageController
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.model.ClientRef
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataDescriptor
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataDescriptorsRepo
import pro.qyoga.core.clients.therapeutic_data.descriptors.findByTherapistId
import pro.qyoga.core.clients.therapeutic_data.values.TherapeuticDataFieldValue
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.clients.TherapeuticDataDescriptorsObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.object_mothers.therapists.idOnlyUserDetails

@Component
class ClientsBackgrounds(
    private val clientsRepo: ClientsRepo,
    private val clientsListPageController: ClientsListPageController,
    private val journalBackgrounds: ClientJournalBackgrounds,
    private val therapeuticDataDescriptorsRepo: TherapeuticDataDescriptorsRepo
) {

    fun aClient(): Client {
        return createClients(1, THE_THERAPIST_ID).single()
    }

    fun createClients(clients: List<ClientCardDto>, therapistId: Long = THE_THERAPIST_ID): Iterable<Client> {
        return clientsRepo.saveAll(clients.map { ClientsObjectMother.createClient(therapistId, it) })
    }

    fun createClients(count: Int, therapistId: Long = THE_THERAPIST_ID): Iterable<Client> {
        return createClients(ClientsObjectMother.createClientCardDtos(count), therapistId)
    }

    fun getAllTherapistClients(therapist: TherapistRef = THE_THERAPIST_REF): List<Client> {
        val model = ExtendedModelMap()
        clientsListPageController.getClients(idOnlyUserDetails(therapist.id!!), Pageable.ofSize(Int.MAX_VALUE), model)
        return ClientsListPageController.getClients(model).content
    }

    fun createClientWithJournalEntry(therapistId: Long = THE_THERAPIST_ID): Pair<Client, JournalEntry> {
        val client = createClients(1, therapistId).single()
        val entry = journalBackgrounds.createEntries(client.id, idOnlyUserDetails(therapistId), 1).single()
        return client to entry
    }

    fun createClient(
        phone: String = faker.phoneNumber().phoneNumberInternational(),
        therapistId: Long = THE_THERAPIST_ID
    ): Client {
        return createClients(listOf(ClientsObjectMother.createClientCardDto(phone = phone)), therapistId).single()
    }

    fun createTherapeuticDataDescriptor(
        therapeuticDataDescriptor: () -> TherapeuticDataDescriptor = { TherapeuticDataDescriptorsObjectMother.therapeuticDataDescriptor() }
    ): TherapeuticDataDescriptor {
        return therapeuticDataDescriptorsRepo.save(therapeuticDataDescriptor())
    }

    fun getTherapeuticDataDescription(therapistId: Long): TherapeuticDataDescriptor? =
        therapeuticDataDescriptorsRepo.findByTherapistId(therapistId)

    fun getTherapeuticData(ref: ClientRef): List<TherapeuticDataFieldValue<*>> {
        TODO()
    }

}