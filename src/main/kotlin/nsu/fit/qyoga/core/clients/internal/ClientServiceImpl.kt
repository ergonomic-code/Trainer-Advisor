package nsu.fit.qyoga.core.clients.internal

import nsu.fit.qyoga.core.clients.api.Client
import nsu.fit.qyoga.core.clients.api.ClientService
import nsu.fit.qyoga.core.clients.api.Dto.ClientListDto
import nsu.fit.qyoga.core.clients.api.Dto.ClientListSearchDto
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ClientServiceImpl(
    private val clientRepo: ClientRepo
) : ClientService {

    fun fromDtoToClients(searchDto: ClientListSearchDto) : Client {
        var client: Client
        client.name = searchDto.name.toString()

        //searchDto.dateAppointment

        return
    }

    override fun getClients(searchDto: ClientListSearchDto, pageable: Pageable): Page<ClientListDto> {
        return clientRepo.findAll(Example.of(searchDto), pageable)
    }
   /* override fun getClients(searchDto: ClientListSearchDto, pageable: Pageable): Page<ClientListDto> {
        val result = clientRepo.getClientsByFilters(
            searchDto,
            pageable.pageNumber * pageable.pageSize,
            pageable.pageSize
        )
        val count = clientRepo.countClients(searchDto)
        return PageImpl(result, pageable, count)
    }*/
}
