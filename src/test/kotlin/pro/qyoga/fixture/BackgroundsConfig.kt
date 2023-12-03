package pro.qyoga.fixture

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import pro.qyoga.core.clients.ClientsConfig
import pro.qyoga.core.programs.exercises.ExercisesConfig


@Import(ClientsConfig::class, ExercisesConfig::class)
@TestConfiguration
@ComponentScan
class BackgroundsConfig