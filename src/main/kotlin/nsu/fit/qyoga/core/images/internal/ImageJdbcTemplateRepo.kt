package nsu.fit.qyoga.core.images.internal

import nsu.fit.qyoga.core.images.api.model.Image
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class ImageJdbcTemplateRepo(
    private val jdbcTemplate: NamedParameterJdbcOperations
) {

    fun findImageList(idList: List<Long>): List<Image> {
        val query = """
            select 
            images.id as imageId,
            images.name AS imageName, 
            images.media_type AS imageMediaType, 
            images.size AS imageSize, 
            images.data AS imageData
            from images
            where images.id IN (:fields)
        """.trimIndent()
        return jdbcTemplate.query(
            query,
            MapSqlParameterSource("fields", idList)
        ) { rs: ResultSet, _: Int ->
            Image(
                id = rs.getLong("imageId"),
                name = rs.getString("imageName"),
                mediaType = rs.getString("imageMediaType"),
                size = rs.getLong("imageSize"),
                data = rs.getBytes("imageData")
            )
        }
    }

    fun delete(id: Long) {
        val query = """
            delete 
            from images
            where images.id = :id
        """.trimIndent()
        jdbcTemplate.update(query, MapSqlParameterSource("id", id))
    }

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

    fun findById(id: Long): Image? {
        val query = """
            SELECT 
            id AS imageId,
            name AS imageName, 
            media_type AS imageMediaType, 
            size AS imageSize, 
            data AS imageData
            FROM images
            WHERE id = :id
        """.trimIndent()
        val imageList: List<Image> = jdbcTemplate.query(
            query,
            MapSqlParameterSource("id", id)
        ) { rs: ResultSet, _: Int ->
            Image(
                id = rs.getLong("imageId"),
                name = rs.getString("imageName"),
                mediaType = rs.getString("imageMediaType"),
                size = rs.getLong("imageSize"),
                data = rs.getBytes("imageData")
            )
        }
        return imageList.firstOrNull()
    }
}
