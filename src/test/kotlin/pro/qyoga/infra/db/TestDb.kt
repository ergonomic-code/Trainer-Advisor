package pro.qyoga.infra.db

import io.minio.MinioClient
import org.flywaydb.core.Flyway
import org.postgresql.ds.PGSimpleDataSource
import org.slf4j.LoggerFactory
import java.net.ConnectException
import java.sql.DriverManager
import java.sql.SQLException

const val PROVIDED_DB_URL = "jdbc:postgresql://localhost:54502/qyoga"
const val MINIO_URL = "http://localhost:50001"

object TestDb

private val log = LoggerFactory.getLogger(TestDb::class.java)

private const val DB_USER = "postgres"
private const val DB_PASSWORD = "password"

private const val MINIO_USER = "user"
private const val MINIO_PASSWORD = "password"

val jdbcUrl: String by lazy {
    try {
        val con = DriverManager.getConnection(
            PROVIDED_DB_URL.replace("qyoga", DB_USER),
            DB_USER,
            DB_PASSWORD
        )
        log.info("Provided db found, recreating it")
        con.prepareStatement(
            """
                DROP DATABASE IF EXISTS qyoga;
                CREATE DATABASE qyoga;
            """.trimIndent()
        )
            .execute()
        log.info("Provided db found, recreated")
        PROVIDED_DB_URL
    } catch (e: SQLException) {
        log.info("Provided Db not found: ${e.message}")
        pgContainer.jdbcUrl
    }
}

val minioUrl: String by lazy {
    try {
        val con = MinioClient.builder()
            .endpoint(MINIO_URL)
            .credentials(MINIO_USER, MINIO_PASSWORD)
            .build()
        // Не нашел нормального способа проверить наличие соединения
        con.listBuckets()
        log.info("minio container found")
        MINIO_URL
    } catch (e: ConnectException) {
        log.info("minio container not found: ${e.message}")
        log.info("http://" + minioContainer.host + ":" + minioContainer.firstMappedPort)
        "http://" + minioContainer.host + ":" + minioContainer.firstMappedPort
    }

}


val testDataSource by lazy {
    migrateSchema(jdbcUrl)
    PGSimpleDataSource().apply {
        this.setUrl(jdbcUrl)
        this.user = DB_USER
        this.password = DB_PASSWORD
    }
}

fun migrateSchema(jdbcUrl: String) {
    log.info("Migrating schema")
    Flyway.configure()
        .dataSource(jdbcUrl, DB_USER, DB_PASSWORD)
        .locations("classpath:/db/migration/common")
        .load()
        .migrate()
    log.info("Schema migrated")
}