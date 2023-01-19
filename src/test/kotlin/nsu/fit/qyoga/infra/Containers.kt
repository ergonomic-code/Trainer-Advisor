package nsu.fit.qyoga.infra

import org.testcontainers.containers.PostgreSQLContainer


val pgContainer by lazy {
    PostgreSQLContainer("postgres:14.2")
        .withExposedPorts(5432)
        .withUsername("postgres")
        .withPassword("P@ssw0rd")
        .withDatabaseName("qyoga")
        .withTmpFs(mapOf("/var" to "rw"))
        .withEnv("PGDATA", "/var/lib/postgresql/data-no-mounted")
        .withReuse(true)
        .apply { start() }
}

