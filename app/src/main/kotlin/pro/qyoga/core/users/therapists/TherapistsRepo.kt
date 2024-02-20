package pro.qyoga.core.users.therapists

import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.util.TypeInformation
import org.springframework.stereotype.Repository


@Repository
class TherapistsRepo(
    jdbcAggregateTemplate: JdbcAggregateTemplate,
    jdbcConverter: JdbcConverter
) : SimpleJdbcRepository<Therapist, Long>(
    jdbcAggregateTemplate,
    BasicPersistentEntity(TypeInformation.of(Therapist::class.java)),
    jdbcConverter
)