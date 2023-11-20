package pro.qyoga.core.programs.exercises

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.infra.db.SdjConfig
import pro.qyoga.infra.images.ImagesConfig

@Import(SdjConfig::class, ImagesConfig::class)
@ComponentScan
@Configuration
class ExercisesConfig