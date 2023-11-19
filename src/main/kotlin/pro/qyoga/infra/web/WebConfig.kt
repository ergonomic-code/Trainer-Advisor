package pro.qyoga.infra.web

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.*
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.data.web.config.SpringDataWebConfiguration


@ImportAutoConfiguration(
    WebMvcAutoConfiguration::class,
    ServletWebServerFactoryAutoConfiguration::class,
    DispatcherServletAutoConfiguration::class,
    ThymeleafAutoConfiguration::class,
    SpringDataWebConfiguration::class,
    ErrorMvcAutoConfiguration::class,
    MultipartAutoConfiguration::class,
    HttpEncodingAutoConfiguration::class
)
@Configuration
class WebConfig