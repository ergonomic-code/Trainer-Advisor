package pro.qyoga.core.clients.files.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.spring.sdj.ergo.ErgoRepository
import pro.azhidkov.platform.spring.sdj.sortBy
import pro.qyoga.core.clients.files.model.ClientFile
import java.util.*
import kotlin.reflect.KProperty1


@Repository
class ClientFilesRepo(
    jdbcAggregateTemplate: JdbcAggregateOperations,
    namedParameterJdbcOperations: NamedParameterJdbcOperations,
    jdbcConverter: JdbcConverter,
    relationalMappingContext: RelationalMappingContext,
) : ErgoRepository<ClientFile, Long>(
    jdbcAggregateTemplate,
    namedParameterJdbcOperations,
    ClientFile::class,
    jdbcConverter,
    relationalMappingContext
) {

    object Page {

        val tenNewest = PageRequest.of(0, 10, sortBy(ClientFile::createdAt).descending())

    }

}

fun ClientFilesRepo.findClientFilesPage(
    clientId: UUID,
    pageRequest: PageRequest,
    fetch: Iterable<KProperty1<ClientFile, *>> = ClientFile.Fetch.fileOnly
): Page<ClientFile> = this.findPage(pageRequest, fetch) {
    ClientFile::clientRef isEqual AggregateReference.to(clientId)
}

fun ClientFilesRepo.findFile(
    clientId: UUID,
    clientFileId: Long
): ClientFile? =
    this.findOne {
        ClientFile::clientRef isEqual clientId
        ClientFile::id isEqual clientFileId
    }