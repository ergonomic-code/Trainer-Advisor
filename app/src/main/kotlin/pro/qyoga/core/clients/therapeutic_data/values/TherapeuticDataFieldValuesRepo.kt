package pro.qyoga.core.clients.therapeutic_data.values

import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity
import org.springframework.data.util.TypeInformation
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.spring.sdj.erpo.ErgoRepository


@Repository
class TherapeuticDataFieldValuesRepo(
    val jdbcAggregateTemplate: JdbcAggregateOperations,
    namedParameterJdbcOperations: NamedParameterJdbcOperations,
    jdbcConverter: JdbcConverter,
    relationalMappingContext: RelationalMappingContext
) : ErgoRepository<TherapeuticDataFieldValue<*>, Long>(
    jdbcAggregateTemplate,
    namedParameterJdbcOperations,
    relationalMappingContext.getRequiredPersistentEntity(TypeInformation.of(TherapeuticDataFieldValue::class.java)) as RelationalPersistentEntity<TherapeuticDataFieldValue<*>>,
    jdbcConverter,
    relationalMappingContext
)

fun TherapeuticDataFieldValuesRepo.findByClientId(clientId: Long): Iterable<TherapeuticDataFieldValue<*>> {
    return findAll {
        TherapeuticDataFieldValue<*>::clientRef isEqual clientId
    }
}
