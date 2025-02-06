package pro.qyoga.infra.db

import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import pro.azhidkov.platform.spring.sdj.DurationToPGIntervalConverter
import pro.azhidkov.platform.spring.sdj.ModuleConverters
import pro.azhidkov.platform.spring.sdj.PGIntervalToDurationConverter


@Configuration
class SdjConfig(
    private val modulesConverters: List<ModuleConverters>
) : AbstractJdbcConfiguration() {

    override fun userConverters(): List<*> {
        return listOf(
            DurationToPGIntervalConverter(),
            PGIntervalToDurationConverter(),
            *modulesConverters.flatMap { it.converters() }.toTypedArray()
        )
    }

}