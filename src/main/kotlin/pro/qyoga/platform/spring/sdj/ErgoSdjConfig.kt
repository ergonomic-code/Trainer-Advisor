package pro.qyoga.platform.spring.sdj

import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.core.mapping.AggregateReference


@Configuration
class ErgoSdjConfig {

    @Bean
    fun aggregateReferenceDeserializer(): com.fasterxml.jackson.databind.Module {
        return SimpleModule("AggregateReferenceDeserializer").apply {
            addDeserializer(AggregateReference::class.java, AggregateReferenceDeserializer())
        }
    }

}