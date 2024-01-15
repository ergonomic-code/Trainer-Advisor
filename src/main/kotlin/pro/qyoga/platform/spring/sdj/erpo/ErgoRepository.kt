package pro.qyoga.platform.spring.sdj.erpo

import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository
import org.springframework.data.mapping.PersistentEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask


class ErgoRepository<T : Any, ID : Any>(
    jdbcAggregateTemplate: JdbcAggregateOperations,
    entity: PersistentEntity<T, *>,
    jdbcConverter: JdbcConverter
) : SimpleJdbcRepository<T, ID>(jdbcAggregateTemplate, entity, jdbcConverter) {

    @Transactional
    fun update(id: ID, func: (T) -> T): T {
        val task = findByIdOrNull(id)
            ?: throw AggregateNotFound(id, TherapeuticTask::class)

        val updatedTask = func(task)
        return save(updatedTask)
    }

}