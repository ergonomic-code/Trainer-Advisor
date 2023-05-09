package nsu.fit.platform.db

import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import java.io.Serializable

@Configuration
class QYogaPlatformJdbcConfig(
    private val converters: MutableList<Converter<out Serializable, out Serializable>>
) : AbstractJdbcConfiguration() {

    override fun userConverters(): MutableList<*> = converters

}
