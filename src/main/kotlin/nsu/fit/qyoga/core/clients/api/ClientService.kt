package nsu.fit.qyoga.core.clients.api

import nsu.fit.qyoga.core.clients.api.Dto.ClientListDto
import nsu.fit.qyoga.core.clients.api.Dto.ClientListSearchDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ClientService {
    fun getClients(
        searchDto: ClientListSearchDto,
        page: Pageable
    ): Page<ClientListDto>
}
