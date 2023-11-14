package pro.qyoga.core.programs.exercises

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.core.programs.exercises.internal.ExercisesRepo
import pro.qyoga.core.programs.exercises.internal.ExercisesServiceImpl
import pro.qyoga.infra.db.SdjConfig
import pro.qyoga.infra.files.FilesConfig

@Import(SdjConfig::class, FilesConfig::class)
@Configuration
class ExercisesConfig(
    val sdjConfig: SdjConfig,
    val filesConfig: FilesConfig
) {

    @Bean
    fun exercisesRepo() = ExercisesRepo(sdjConfig.jdbcAggregateTemplate(), sdjConfig.jdbcConverter())

    @Bean
    fun exercisesService() =
        ExercisesServiceImpl(exercisesRepo(), filesConfig.imagesService())

}