package pro.qyoga.infra.db

import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import pro.qyoga.platform.spring.sdj.DurationToPGIntervalConverter
import pro.qyoga.platform.spring.sdj.PGIntervalToDurationConverter


@Configuration
class SdjConfig : AbstractJdbcConfiguration() {

    override fun userConverters(): List<*> {
        return listOf(
            DurationToPGIntervalConverter(),
            PGIntervalToDurationConverter()
        )
    }

}