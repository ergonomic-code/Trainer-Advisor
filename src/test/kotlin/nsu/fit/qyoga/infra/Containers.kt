package nsu.fit.qyoga.infra

import org.testcontainers.containers.PostgreSQLContainer

val pgContainer by lazy {
    PostgreSQLContainer("postgres:14.2")
        .withExposedPorts(5432)
        .withUsername("postgres")
        .withPassword("P@ssw0rd")
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
