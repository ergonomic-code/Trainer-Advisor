package pro.qyoga.infra.web

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.*
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.web.config.SpringDataWebConfiguration


@ImportAutoConfiguration(
    WebMvcAutoConfiguration::class,
    ServletWebServerFactoryAutoConfiguration::class,
    DispatcherServletAutoConfiguration::class,
    SpringDataWebConfiguration::class,
    ErrorMvcAutoConfiguration::class,
    MultipartAutoConfiguration::class,
    HttpEncodingAutoConfiguration::class
)
@Import(ThymeleafConfig::class)
@Configuration
class WebConfig