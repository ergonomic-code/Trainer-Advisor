package pro.qyoga.infra.web

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.standard.StandardDialect
import org.thymeleaf.standard.serializer.IStandardJavaScriptSerializer


@Configuration
class ThymeleafConfig(
    private val objectMapper: ObjectMapper, private val engine: SpringTemplateEngine
) {

    /**
     * Не нашёл другого способа подложить objectMapper в thymeleaf.
     * А захардкоженный маппер игнорирует @JsonUnwrapped, и подтянутые агрегаты криво рендярятся в страницы
     */
    @PostConstruct
    fun patchStandardDialect() {
        engine.dialects.filterIsInstance<StandardDialect>().single().javaScriptSerializer =
            IStandardJavaScriptSerializer { obj, writer ->
                val jsonStr = objectMapper.writeValueAsString(obj)
                writer.write(jsonStr)
            }
    }

}