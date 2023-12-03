package pro.qyoga.core.clients.internal

import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.core.clients.api.ClientCardDto
import pro.qyoga.core.clients.api.ClientSearchDto
import pro.qyoga.core.clients.api.ClientsService
import pro.qyoga.platform.spring.sdj.example
import pro.qyoga.platform.spring.sdj.probeFrom
import pro.qyoga.platform.spring.sdj.sortBy


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
        client = client.updateBy(clientCardDto)
        return clientsRepo.save(client)
    }

    override fun findClient(id: Long): Client? {
        return clientsRepo.findByIdOrNull(id)
    }

    override fun findClients(searchDto: ClientSearchDto, page: Pageable): Page<Client> {
        val example = example<Client>(probeFrom(searchDto)) {
            withMatcher(searchDto::firstName, ExampleMatcher.GenericPropertyMatcher().contains().ignoreCase())
            withMatcher(searchDto::lastName, ExampleMatcher.GenericPropertyMatcher().contains().ignoreCase())
            withMatcher(searchDto::middleName, ExampleMatcher.GenericPropertyMatcher().contains().ignoreCase())
            withMatcher(searchDto::phoneNumber, ExampleMatcher.GenericPropertyMatcher().contains().ignoreCase())
        }

        val pageRequest = PageRequest.of(page.pageNumber, page.pageSize, sortBy(Client::lastName))

        return clientsRepo.findAll(example, pageRequest)
    }

    @Transactional
    override fun deleteClient(id: Long) {
        clientsRepo.deleteById(id)
    }

}