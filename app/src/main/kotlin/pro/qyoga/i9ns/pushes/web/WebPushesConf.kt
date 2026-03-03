package pro.qyoga.i9ns.pushes.web

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import pro.azhidkov.platform.spring.sdj.converters.ModuleConverters
import pro.azhidkov.platform.spring.sdj.converters.ObjectToJsonbConverters.convertersFor
import pro.qyoga.i9ns.pushes.web.model.WebPushSubscription
import tools.jackson.databind.ObjectMapper


@Configuration
@ComponentScan
@EnableConfigurationProperties(WebPushesConfProps::class)
class WebPushesConf {

    @Bean
    fun webPushesConverters(objectMapper: ObjectMapper) = ModuleConverters {
        convertersFor<WebPushSubscription>(objectMapper)
    }

}
