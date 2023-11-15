package pro.qyoga.infra.files.internal

import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.util.TypeInformation
import pro.qyoga.infra.files.api.File

class FilesRepo(
    jdbcAggregateTemplate: JdbcAggregateTemplate,
    jdbcConverter: JdbcConverter
) : SimpleJdbcRepository<File, Long>(
    jdbcAggregateTemplate,
    BasicPersistentEntity(TypeInformation.of(File::class.java)),
    jdbcConverter
)
