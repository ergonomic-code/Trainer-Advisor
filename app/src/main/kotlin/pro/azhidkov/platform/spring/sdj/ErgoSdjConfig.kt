package pro.azhidkov.platform.spring.sdj

import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.core.mapping.AggregateReference
import pro.azhidkov.platform.spring.sdj.ergo.ErgoPersistenceExceptionTranslator
import com.fasterxml.jackson.databind.Module as JacksonModule


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

    @Bean
    fun persistenceExceptionTranslator() =
        ErgoPersistenceExceptionTranslator()

}