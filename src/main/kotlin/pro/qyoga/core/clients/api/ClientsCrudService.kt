package pro.qyoga.core.clients.api

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface ClientsCrudService {

    fun saveAll(clientDtos: List<ClientDto>): List<ClientDto>

    fun findClients(searchDto: ClientSearchDto, page: Pageable): Page<ClientDto>

    fun deleteById(id: Long)

}