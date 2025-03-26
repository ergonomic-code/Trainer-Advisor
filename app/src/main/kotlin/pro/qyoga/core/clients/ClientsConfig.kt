package pro.qyoga.core.clients

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import pro.azhidkov.platform.spring.sdj.converters.ModuleConverters
import pro.qyoga.core.clients.cards.model.PhoneNumberToStringConverter
import pro.qyoga.core.clients.cards.model.StringToPhoneNumberConverter


@ComponentScan
@Configuration
class ClientsConfig {

    @Bean
    fun clientsConverters() = ModuleConverters {
        setOf(PhoneNumberToStringConverter(), StringToPhoneNumberConverter())
    }

}