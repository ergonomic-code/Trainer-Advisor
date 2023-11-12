package pro.qyoga.core.clients

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.core.clients.api.ClientsCrudService
import pro.qyoga.core.clients.internal.ClientRepo
import pro.qyoga.infra.db.SdjConfig


@Import(SdjConfig::class)
@Configuration
class ClientsConfig(
    private val sdjConfig: SdjConfig
) {

    @Bean
    fun clientsService(): ClientsCrudService =
        ClientRepo(sdjConfig.jdbcAggregateTemplate(), sdjConfig.jdbcConverter())

}