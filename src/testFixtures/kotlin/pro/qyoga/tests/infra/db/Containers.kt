package pro.qyoga.tests.infra.db

import io.minio.MinioClient
import org.testcontainers.containers.MinIOContainer
import org.testcontainers.containers.PostgreSQLContainer

val pgContainer: PostgreSQLContainer<*> by lazy {
    PostgreSQLContainer("postgres:15.2")
        .withExposedPorts(5432)
        .withUsername("postgres")
        .withPassword("password")
        .withDatabaseName("postgres")
        .withTmpFs(mapOf("/var" to "rw"))
        .withEnv("PGDATA", "/var/lib/postgresql/data-no-mounted")
        .withReuse(true)
        .withInitScript("db/qyoga-db-init.sql")
        .apply {
            start()
            // Сначала подключаемся к postgres, пересоздаём qyoga для обнуления фикстуры и подключаемся к ней
            this.withDatabaseName("qyoga")
        }
}

val minioContainer: MinIOContainer by lazy {
    MinIOContainer("minio/minio")
        .withExposedPorts(9000)
        .withUserName("user")
        .withPassword("password")
        .withTmpFs(mapOf("/tmp" to "rw"))
        .withReuse(true)
        .withEnv("MINIO_VOLUMES", "/tmp/minio")
        .withCommand("server")
        .apply {
            start()
        }
}
