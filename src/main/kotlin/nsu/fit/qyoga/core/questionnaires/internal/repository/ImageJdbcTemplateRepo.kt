package nsu.fit.qyoga.core.questionnaires.internal.repository

import nsu.fit.qyoga.core.questionnaires.api.model.Image
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository


@Repository
class ImageJdbcTemplateRepo(
    private val jdbcTemplate: NamedParameterJdbcOperations
) {

    fun save(image: Image): Long {
        val query = """
            INSERT INTO images (name, media_type, size, data) values (:name, :mediaType, :size, :data) RETURNING id
        """.trimIndent()
        val params = MapSqlParameterSource()
        params.addValue("name", image.name)
        params.addValue("mediaType", image.mediaType)
        params.addValue("size", image.size)
        params.addValue("data", image.data)
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(query, params, keyHolder)
        return keyHolder.key!!.toLong()
    }

    fun findById(id: Long) {

    }
}