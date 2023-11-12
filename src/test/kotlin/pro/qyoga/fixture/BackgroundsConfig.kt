package pro.qyoga.fixture

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import pro.qyoga.core.clients.ClientsConfig
import pro.qyoga.core.programs.exercises.ExercisesConfig


@Import(ClientsConfig::class)
@TestConfiguration
class BackgroundsConfig(
    private val clientsConfig: ClientsConfig,
    private val exercisesConfig: ExercisesConfig
) {

    @Bean
    fun backgrounds() = Backgrounds(
        ClientsBackgrounds(clientsConfig.clientsService()),
        ExerciseBackgrounds(exercisesConfig.exercisesService())
    )

}