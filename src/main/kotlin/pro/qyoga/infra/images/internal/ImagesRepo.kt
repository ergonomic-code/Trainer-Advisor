package pro.qyoga.infra.images.internal

import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.util.TypeInformation
import pro.qyoga.infra.images.api.File

class ImagesRepo(
    jdbcAggregateTemplate: JdbcAggregateTemplate,
    jdbcConverter: JdbcConverter
) : SimpleJdbcRepository<File, Long>(
    jdbcAggregateTemplate,
    BasicPersistentEntity(TypeInformation.of(File::class.java)),
    jdbcConverter
)
