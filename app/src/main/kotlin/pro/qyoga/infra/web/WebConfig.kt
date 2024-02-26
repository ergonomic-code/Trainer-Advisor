package pro.qyoga.infra.web

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


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