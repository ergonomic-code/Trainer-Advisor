package nsu.fit.qyoga.infra

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource


class TestContainerDbContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        applicationContext.environment.propertySources.addFirst(
            MapPropertySource(
                "Integration postgres test properties",
                mapOf(
                    "spring.datasource.url" to pgContainer.jdbcUrl,
                    "spring.datasource.username" to pgContainer.username,
                    "spring.datasource.password" to pgContainer.password
                )
            )
        )
    }

}