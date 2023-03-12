package nsu.fit.qyoga.core.clients.internal

import nsu.fit.qyoga.core.clients.api.Client
import nsu.fit.qyoga.core.clients.api.Dto.ClientListDto
import nsu.fit.qyoga.core.clients.api.Dto.ClientListSearchDto
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = false)
interface ClientRepo: CrudRepository<Client, Long>, PagingAndSortingRepository<Client, Long> {
    @Query(
        """
        SELECT cl.id as id, _name, secondname, surname, phone_number, diagnose, ap.date_app as dateAppointment
        FROM clients cl
            INNER JOIN appointment ap ON cl.id = ap.client_id
        WHERE (cl._name LIKE '%' || :#{#search.name ?: ""} || '%' OR :#{#search.name ?: ""} IS NULL)
            AND (cl.secondname LIKE '%' || :#{#search.secondname ?: ""} || '%' OR :#{#search.secondname ?: ""} IS NULL)
            AND (cl.surname LIKE '%' || :#{#search.surname ?: ""} || '%' OR :#{#search.surname ?: ""} IS NULL)
            AND (cl.phone_number LIKE '%' || :#{#search?.phoneNumber ?: ""} || '%' OR :#{#search.phoneNumber ?: ""} IS NULL)
            AND (cl.diagnose LIKE '%' || :#{#search.diagnose?.name ?: ""} || '%' OR :#{#search.diagnose ?: ""} IS NULL)
            AND (ap.date_app::varchar LIKE '%' || :#{#search.dateAppointment ?: ""} || '%' OR :#{#search.dateAppointment ?: ""} IS NULL)
        ORDER BY cl.surname
        LIMIT :pageSize OFFSET :offset
    """
    )
    fun getClientsByFilters(
        @Param("search") search: ClientListSearchDto,
        offset: Int,
        pageSize: Int
    ): List<ClientListDto>

    @Query(
        """
        SELECT count(*) 
        FROM clients cl
            INNER JOIN appointment ap ON cl.id = ap.client_id
        WHERE (cl._name LIKE '%' || :#{#search.name ?: ""} || '%' OR :#{#search.name ?: ""} IS NULL)
            AND (cl.secondname LIKE '%' || :#{#search.secondname ?: ""} || '%' OR :#{#search.secondname ?: ""} IS NULL)
            AND (cl.surname LIKE '%' || :#{#search.surname ?: ""} || '%' OR :#{#search.surname ?: ""} IS NULL)
            AND (cl.phone_number LIKE '%' || :#{#search?.phoneNumber ?: ""} || '%' OR :#{#search.phoneNumber ?: ""} IS NULL)
            AND (cl.diagnose LIKE '%' || :#{#search.diagnose?.name ?: ""} || '%' OR :#{#search.diagnose ?: ""} IS NULL)
            AND (ap.date_app::varchar LIKE '%' || :#{#search.dateAppointment ?: ""} || '%' OR :#{#search.dateAppointment ?: ""} IS NULL)
    """
    )
    fun countClients(
        @Param("search") search: ClientListSearchDto,
    ): Long

}