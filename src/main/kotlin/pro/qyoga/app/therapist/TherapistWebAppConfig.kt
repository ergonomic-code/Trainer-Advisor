package pro.qyoga.app.therapist

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.core.clients.ClientsConfig
import pro.qyoga.core.programs.exercises.ExercisesConfig


@Import(ClientsConfig::class, ExercisesConfig::class)
@ComponentScan
@Configuration
class TherapistWebAppConfig