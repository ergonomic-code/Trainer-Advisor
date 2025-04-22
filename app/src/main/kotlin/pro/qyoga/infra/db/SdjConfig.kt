package pro.qyoga.infra.db

import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing
import pro.azhidkov.platform.spring.sdj.converters.*


@EnableJdbcAuditing
@Configuration
class SdjConfig(
    private val modulesConverters: List<ModuleConverters>
) : AbstractJdbcConfiguration() {

    override fun userConverters(): List<*> {
        return listOf(
            DurationToPGIntervalConverter(),
            PGIntervalToDurationConverter(),
            URLToStringConverter(),
            StringToURLConverter(),
            *modulesConverters.flatMap { it.converters() }.toTypedArray()
        )
    }

}