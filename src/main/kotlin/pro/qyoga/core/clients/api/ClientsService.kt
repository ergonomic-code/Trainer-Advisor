package pro.qyoga.core.clients.api

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface ClientsService {

    fun createClients(therapistId: Long, clientCardDtos: List<ClientCardDto>): Iterable<Client>

    fun editClient(clientId: Long, clientCardDto: ClientCardDto): Client

    fun findClient(id: Long): Client?

    fun findClients(therapistId: Long, searchDto: ClientSearchDto, pageRequest: Pageable): Page<Client>

    fun deleteClient(id: Long)

}