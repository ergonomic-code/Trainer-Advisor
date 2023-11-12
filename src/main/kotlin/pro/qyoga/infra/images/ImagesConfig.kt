package pro.qyoga.infra.images

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.infra.db.SdjConfig
import pro.qyoga.infra.images.internal.ImagesRepo
import pro.qyoga.infra.images.internal.ImagesServiceImpl

@Import(SdjConfig::class)
@Configuration
class ImagesConfig(
    private val sdjConfig: SdjConfig
) {

    @Bean
    fun imagesRepo() = ImagesRepo(sdjConfig.jdbcAggregateTemplate(), sdjConfig.jdbcConverter())

    @Bean
    fun imagesService() = ImagesServiceImpl(imagesRepo())

}