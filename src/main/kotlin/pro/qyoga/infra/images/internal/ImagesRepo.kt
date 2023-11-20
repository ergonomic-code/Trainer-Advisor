package pro.qyoga.infra.images.internal

import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.util.TypeInformation
import org.springframework.stereotype.Repository
import pro.qyoga.infra.images.api.Image

@Repository
class ImagesRepo(
    jdbcAggregateTemplate: JdbcAggregateOperations,
    jdbcConverter: JdbcConverter
) : SimpleJdbcRepository<Image, Long>(
    jdbcAggregateTemplate,
    BasicPersistentEntity(TypeInformation.of(Image::class.java)),
    jdbcConverter
)
