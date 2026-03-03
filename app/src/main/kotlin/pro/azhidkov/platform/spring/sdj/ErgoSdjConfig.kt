package pro.azhidkov.platform.spring.sdj

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.core.mapping.AggregateReference
import tools.jackson.databind.JacksonModule
import tools.jackson.databind.module.SimpleModule


@Configuration
class ErgoSdjConfig {

    @Bean
    fun aggregateReferenceDeserializer(): JacksonModule =
        SimpleModule("AggregateReferenceDeserializer").apply {
            addDeserializer(AggregateReference::class.java, AggregateReferenceDeserializer())
        }

    @Bean
    fun aggregateReferenceBindingAdvice() =
        AggregateReferenceBindingAdvice()

}
