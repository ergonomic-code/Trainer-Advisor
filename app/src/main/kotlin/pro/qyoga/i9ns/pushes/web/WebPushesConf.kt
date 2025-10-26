package pro.qyoga.i9ns.pushes.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import pro.azhidkov.platform.spring.sdj.converters.JacksonJsonbToObjectReader
import pro.azhidkov.platform.spring.sdj.converters.JacksonObjectToJsonbWriter
import pro.azhidkov.platform.spring.sdj.converters.ModuleConverters
import pro.qyoga.i9ns.pushes.web.model.WebPushSubscription


@Configuration
@ComponentScan
@EnableConfigurationProperties(WebPushesConfProps::class)
class WebPushesConf {

    @Bean
    fun webPushesConverters(objectMapper: ObjectMapper) = ModuleConverters {
        setOf(
            object : JacksonObjectToJsonbWriter<WebPushSubscription>(objectMapper) {},
            object : JacksonJsonbToObjectReader<WebPushSubscription>(objectMapper, WebPushSubscription::class) {},
        )
    }

}
