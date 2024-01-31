package pro.qyoga.infra.timezones

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.infra.locale.qyogaLocale


@Configuration
class TimeZonesConfig {

    @Bean
    fun timeZones() = TimeZones(qyogaLocale)

}