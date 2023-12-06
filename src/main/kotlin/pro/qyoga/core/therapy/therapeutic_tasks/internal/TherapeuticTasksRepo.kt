package pro.qyoga.core.therapy.therapeutic_tasks.internal

import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.util.TypeInformation
import org.springframework.stereotype.Repository
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask


@Repository
class TherapeuticTasksRepo(
    jdbcAggregateTemplate: JdbcAggregateOperations,
    jdbcConverter: JdbcConverter
) : SimpleJdbcRepository<TherapeuticTask, Long>(
    jdbcAggregateTemplate,
    BasicPersistentEntity(TypeInformation.of(TherapeuticTask::class.java)),
    jdbcConverter
)