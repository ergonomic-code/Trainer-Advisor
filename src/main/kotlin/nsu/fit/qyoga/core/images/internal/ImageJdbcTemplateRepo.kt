package nsu.fit.qyoga.core.images.internal

import nsu.fit.qyoga.core.images.api.error.ImageException
import nsu.fit.qyoga.core.images.api.model.Image
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class ImageJdbcTemplateRepo(
    private val jdbcTemplate: NamedParameterJdbcOperations
) {

    fun findImageList(idList: List<Long>): List<Image> {
        val imageList = mutableListOf<Image>()
        val query = """
            select 
            t.id as imageId,
            images.name AS imageName, 
            images.media_type AS imageMediaType, 
            images.size AS imageSize, 
            images.data AS imageData
            from (
              values ${idListToValues(idList)}
            ) as t(id)
            left join images on images.id = t.id
        """.trimIndent()
        jdbcTemplate.query(
            query
        ) { rs: ResultSet, _: Int ->
            imageList.add(
                Image(
                    id = rs.getLong("imageId"),
                    name = rs.getString("imageName")
                        ?: throw ImageException("No existing image with id = ${rs.getLong("imageId")}"),
                    mediaType = rs.getString("imageMediaType"),
                    size = rs.getLong("imageSize"),
                    data = rs.getBytes("imageData")
                )
            )
        }
        return imageList
    }

    fun idListToValues(idList: List<Long>): String {
        return buildString {
            for (id in idList) {
                append("($id),")
            }
        }.dropLast(1)
    }
}
