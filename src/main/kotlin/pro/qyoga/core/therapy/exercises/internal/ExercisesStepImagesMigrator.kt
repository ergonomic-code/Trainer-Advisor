package pro.qyoga.core.therapy.exercises.internal

import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Component
import pro.qyoga.platform.file_storage.api.FileMetaData
import pro.qyoga.platform.file_storage.api.StoredFile
import java.util.stream.Stream

@Component
class ExercisesStepImagesMigrator(
    private val jdbcOperations: NamedParameterJdbcOperations, private val minioClient: MinioClient
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun migrate(e: ApplicationReadyEvent) {
        log.info("Starting exercise steps images migration")
        val files = fetchNotMigratedFiles()

        files.forEach { file ->
            val putCommand =
                PutObjectArgs.builder()
                    .bucket("exercise.steps")
                    .`object`(file.metaData.name)
                    .contentType(file.metaData.mediaType)
                    .stream(file.content.inputStream(), file.metaData.size, -1)
                    .build()

            minioClient.putObject(putCommand)

            markMigrated(file.metaData.id)
            log.info("Image {} has been migrated", file.metaData.id)
        }
    }

    private fun fetchNotMigratedFiles(): Stream<StoredFile> {
        val sql = """
                SELECT *
                FROM files
                WHERE size > 0 AND data IS NOT NULL
            """.trimIndent()
        val rowMapper = DataClassRowMapper(FileMetaData::class.java)
        return jdbcOperations.queryForStream(sql, emptyMap<String, Any>()) { rs, rowNum ->
            StoredFile(rowMapper.mapRow(rs, rowNum), rs.getBytes("data"))
        }
    }

    private fun markMigrated(fileId: Long) {
        val sql = """
            UPDATE files
            SET size = 0
            WHERE id = :fileId
        """.trimIndent()
        jdbcOperations.update(sql, mapOf("fileId" to fileId))
    }

}