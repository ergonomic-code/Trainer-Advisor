package nsu.fit.qyoga.app

import nsu.fit.platform.db.DurationToPGIntervalConverter
import nsu.fit.platform.db.PGIntervalToDurationConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import java.io.Serializable

@Configuration
class QYogaAppJdbcConfig {

    @Bean
    fun converters(): MutableList<Converter<out Serializable, out Serializable>> = mutableListOf(
        DurationToPGIntervalConverter(),
        PGIntervalToDurationConverter()
    )

}
