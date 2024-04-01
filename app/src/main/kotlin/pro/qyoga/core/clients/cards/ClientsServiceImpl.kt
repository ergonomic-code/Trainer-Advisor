package pro.qyoga.core.clients.cards

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pro.azhidkov.platform.spring.sdj.withSortBy
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.dtos.ClientSearchDto
import pro.qyoga.core.clients.cards.model.Client


@Service
class ClientsServiceImpl(
    private val clientsRepo: ClientsRepo
) : ClientsService {

    @Transactional
    override fun createClients(therapistId: Long, clientCardDtos: List<ClientCardDto>): Iterable<Client> {
        val clients = clientCardDtos.map { Client(therapistId, it) }
        return clientsRepo.saveAll(clients)
    }

    override fun editClient(clientId: Long, clientCardDto: ClientCardDto): Client {
        var client = clientsRepo.findByIdOrNull(clientId)
        checkNotNull(client) { "Клиент $clientId не найде" }
        client = client.patchedBy(clientCardDto)
        return clientsRepo.save(client)
    }

    override fun findClient(id: Long): Client? {
        return clientsRepo.findByIdOrNull(id)
    }

    override fun findClients(therapistId: Long, searchDto: ClientSearchDto, pageRequest: Pageable): Page<Client> {
        return clientsRepo.findBy(therapistId, searchDto, pageRequest.withSortBy(Client::lastName))
    }

    @Transactional
    override fun deleteClient(id: Long) {
        clientsRepo.deleteById(id)
    }

    override fun clientExists(id: Long): Boolean {
        return clientsRepo.existsById(id)
    }

}