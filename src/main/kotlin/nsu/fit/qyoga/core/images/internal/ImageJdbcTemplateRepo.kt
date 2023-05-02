package nsu.fit.qyoga.core.images.internal

import nsu.fit.platform.errors.ResourceNotFound
import nsu.fit.qyoga.core.images.api.model.Image
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class ImageJdbcTemplateRepo(
    private val jdbcTemplate: NamedParameterJdbcOperations
) {
    private val query = """
        select 
        id as imageId,
        images.name AS imageName, 
        images.media_type AS imageMediaType, 
        images.size AS imageSize, 
        images.data AS imageData
        from (
          values :values
        ) as t(id)
        left join images on images.id = t.id
    """.trimIndent()

    fun findImageList(idList: List<Long>): List<Image> {
        val imageList = mutableListOf<Image>()
        jdbcTemplate.query(
            query,
            MapSqlParameterSource("values", idListToValues(idList))
        ) { rs: ResultSet, _: Int ->
            imageList.add(
                Image(
                    id = rs.getLong("imageId"),
                    name = rs.getString("imageName")
                        ?: throw ResourceNotFound("No existing image with id = ${rs.getLong("imageId")}"),
                    mediaType = rs.getString("imageMediaType"),
                    size = rs.getLong("imageSize"),
                    data = rs.getBytes("imageData")
                )
            )
        }
        return imageList
    }

    fun idListToValues(idList: List<Long>): String = buildString {
        for (id in idList)
            append("($id)")
    }.dropLast(1)
}
