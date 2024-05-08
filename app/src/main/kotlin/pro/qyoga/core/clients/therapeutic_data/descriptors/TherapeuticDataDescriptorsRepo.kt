package pro.qyoga.core.clients.therapeutic_data.descriptors

import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.util.TypeInformation
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.spring.sdj.erpo.ErgoRepository


@Repository
class TherapeuticDataDescriptorsRepo(
    jdbcAggregateTemplate: JdbcAggregateOperations,
    namedParameterJdbcOperations: NamedParameterJdbcOperations,
    jdbcConverter: JdbcConverter,
    relationalMappingContext: RelationalMappingContext
) : ErgoRepository<TherapeuticDataDescriptor, Long>(
    jdbcAggregateTemplate,
    namedParameterJdbcOperations,
    BasicPersistentEntity(TypeInformation.of(TherapeuticDataDescriptor::class.java)),
    jdbcConverter,
    relationalMappingContext
) {

    override fun <S : TherapeuticDataDescriptor?> save(instance: S & Any): S & Any {
        // см. https://github.com/d-r-q/QYoga/issues/204
        val hasUpdatesAndAdditions = instance.blocks.map { it.id == 0L }.toSet().size > 1
                || instance.fields.map { it.id == 0L }.toSet().size > 1

        val persisted = super.save(instance)

        return if (hasUpdatesAndAdditions) {
            @Suppress("UNCHECKED_CAST")
            findByIdOrNull(persisted.id)!! as (S & Any)
        } else {
            persisted
        }
    }
}

fun TherapeuticDataDescriptorsRepo.findByTherapistId(therapistId: Long): TherapeuticDataDescriptor? =
    this.findOne {
        TherapeuticDataDescriptor::ownerRef isEqual therapistId
    }
