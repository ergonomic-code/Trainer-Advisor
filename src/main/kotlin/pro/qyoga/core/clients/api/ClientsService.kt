package pro.qyoga.core.clients.api

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import pro.qyoga.core.clients.internal.Client


interface ClientsService {

    fun createClients(therapistId: Long, createClientRequests: List<CreateClientRequest>): Iterable<Client>

    fun findClients(searchDto: ClientSearchDto, page: Pageable): Page<Client>

    fun deleteClient(id: Long)

}