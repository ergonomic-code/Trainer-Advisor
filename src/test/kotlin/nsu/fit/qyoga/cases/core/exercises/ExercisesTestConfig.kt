package nsu.fit.qyoga.cases.core.exercises

import nsu.fit.qyoga.core.exercises.ExercisesConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.context.annotation.Import

@Import(ExercisesConfig::class)
@SpringBootApplication(exclude = [WebMvcAutoConfiguration::class, FlywayAutoConfiguration::class])
class ExercisesTestConfig