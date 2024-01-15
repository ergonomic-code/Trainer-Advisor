package pro.qyoga.infra.web

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.*
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.web.config.SpringDataWebConfiguration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@ImportAutoConfiguration(
    WebMvcAutoConfiguration::class,
    ServletWebServerFactoryAutoConfiguration::class,
    DispatcherServletAutoConfiguration::class,
    SpringDataWebConfiguration::class,
    ErrorMvcAutoConfiguration::class,
    MultipartAutoConfiguration::class,
    HttpEncodingAutoConfiguration::class,
    // Включает поддержку X-Forwarded-* заголовков
    EmbeddedWebServerFactoryCustomizerAutoConfiguration::class
)
@Import(ThymeleafConfig::class)
@Configuration
class WebConfig(
    private val handlerMethodArgumentResolvers: List<HandlerMethodArgumentResolver>
) : WebMvcConfigurer {

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver?>) {
        handlerMethodArgumentResolvers.forEach {
            argumentResolvers.add(it)
        }
    }

}