package pro.qyoga.core.programs.exercises

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.core.programs.exercises.internal.ExercisesRepo
import pro.qyoga.core.programs.exercises.internal.ExercisesServiceImpl
import pro.qyoga.infra.db.SdjConfig
import pro.qyoga.infra.images.ImagesConfig

@Import(SdjConfig::class, ImagesConfig::class)
@Configuration
class ExercisesConfig(
    val sdjConfig: SdjConfig,
    val imagesConfig: ImagesConfig
) {

    @Bean
    fun exercisesRepo() = ExercisesRepo(sdjConfig.jdbcAggregateTemplate(), sdjConfig.jdbcConverter())

    @Bean
    fun exercisesService() =
        ExercisesServiceImpl(exercisesRepo(), imagesConfig.imagesService())

}