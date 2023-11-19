package pro.qyoga.core.users.internal

import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.util.TypeInformation
import org.springframework.stereotype.Repository
import pro.qyoga.core.users.api.Therapist


@Repository
class TherapistsRepo(
    jdbcAggregateTemplate: JdbcAggregateTemplate,
    jdbcConverter: JdbcConverter
) : SimpleJdbcRepository<Therapist, Long>(
    jdbcAggregateTemplate,
    BasicPersistentEntity(TypeInformation.of(Therapist::class.java)),
    jdbcConverter
)