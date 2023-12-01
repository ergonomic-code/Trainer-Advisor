package pro.qyoga.core.clients.internal

import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.core.clients.api.ClientSearchDto
import pro.qyoga.core.clients.api.ClientsService
import pro.qyoga.core.clients.api.CreateClientRequest
import pro.qyoga.platform.spring.sdj.example
import pro.qyoga.platform.spring.sdj.probeFrom
import pro.qyoga.platform.spring.sdj.sortBy


@Service
class ClientsServiceImpl(
    private val clientsRepo: ClientsRepo
) : ClientsService {

    @Transactional
    override fun createClients(therapistId: Long, createClientRequests: List<CreateClientRequest>): Iterable<Client> {
        val clients = createClientRequests.map { Client(therapistId, it) }
        return clientsRepo.saveAll(clients)
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