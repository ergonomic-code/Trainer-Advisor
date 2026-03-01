package pro.qyoga.tests.infra.db

import org.testcontainers.containers.MinIOContainer
import org.testcontainers.postgresql.PostgreSQLContainer
import org.testcontainers.utility.MountableFile

val pgContainer: PostgreSQLContainer by lazy {
    PostgreSQLContainer("postgres:15.2")
        .withExposedPorts(5432)
        .withUsername("postgres")
        .withPassword("password")
        .withDatabaseName("postgres")
        .withTmpFs(mapOf("/var" to "rw"))
        .withEnv("PGDATA", "/var/lib/postgresql/data-no-mounted")
        .withReuse(true)
        .withInitScript("db/qyoga-db-init.sql")
        .withCopyFileToContainer(
            MountableFile.forClasspathResource("/db/pg-initdb.d/qyoga-baseline-250302.psql"),
            "/docker-entrypoint-initdb.d/qyoga-baseline-250302.psql"
        )
        .withCopyFileToContainer(
            MountableFile.forClasspathResource("db/pg-initdb.d/init.sh"),
            "/docker-entrypoint-initdb.d/init.sh"
        )
        .apply {
            start()
            // Сначала подключаемся к postgres, пересоздаём qyoga для обнуления фикстуры и подключаемся к ней
            this.withDatabaseName("qyoga")
        }
}

val minioContainer: MinIOContainer by lazy {
    MinIOContainer("minio/minio:RELEASE.2024-01-16T16-07-38Z")
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
