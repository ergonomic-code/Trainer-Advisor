package pro.qyoga.infra.timezones

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.l10n.systemLocale


@Configuration
class TimeZonesConfig {

    @Bean
    fun timeZones() = TimeZones(systemLocale)

}