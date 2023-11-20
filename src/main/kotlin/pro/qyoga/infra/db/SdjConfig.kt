package pro.qyoga.infra.db

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import pro.qyoga.platform.spring.sdj.DurationToPGIntervalConverter
import pro.qyoga.platform.spring.sdj.PGIntervalToDurationConverter


@ImportAutoConfiguration(
    DataSourceAutoConfiguration::class,
    FlywayAutoConfiguration::class,
    JdbcRepositoriesAutoConfiguration::class,
    JdbcTemplateAutoConfiguration::class,
    DataSourceTransactionManagerAutoConfiguration::class
)
@EnableJdbcRepositories
@Configuration
class SdjConfig : AbstractJdbcConfiguration() {

    override fun userConverters(): List<*> {
        return listOf(
            DurationToPGIntervalConverter(),
            PGIntervalToDurationConverter()
        )
    }

}