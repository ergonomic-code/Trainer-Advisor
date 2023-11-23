package pro.qyoga.infra.db

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource

class TestInfraContextInitializer :
    ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        applicationContext.environment.propertySources.addFirst(
            MapPropertySource(
                "Integration postgres test properties",
                mapOf(
                    "spring.datasource.url" to jdbcUrl
                )
            )
        )
        applicationContext.environment.propertySources.addFirst(
            MapPropertySource(
                "Integration minio test properties",
                mapOf(
                    "spring.datasource.url" to minioUrl
                )
            )
        )
    }

}