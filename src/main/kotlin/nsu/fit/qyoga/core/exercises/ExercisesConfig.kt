package nsu.fit.qyoga.core.exercises

import nsu.fit.platform.db.QYogaPlatformJdbcConfig
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@Import(QYogaPlatformJdbcConfig::class)
@ComponentScan
@Configuration
@EnableJdbcRepositories
class ExercisesConfig
