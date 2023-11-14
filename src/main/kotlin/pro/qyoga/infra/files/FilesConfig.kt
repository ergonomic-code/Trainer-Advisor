package pro.qyoga.infra.files

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.infra.db.SdjConfig
import pro.qyoga.infra.files.internal.FilesRepo
import pro.qyoga.infra.files.internal.FilesServiceImpl

@Import(SdjConfig::class)
@Configuration
class FilesConfig(
    private val sdjConfig: SdjConfig
) {

    @Bean
    fun imagesRepo() = FilesRepo(sdjConfig.jdbcAggregateTemplate(), sdjConfig.jdbcConverter())

    @Bean
    fun imagesService() = FilesServiceImpl(imagesRepo())

}