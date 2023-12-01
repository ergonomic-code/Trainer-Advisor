package pro.qyoga.fixture

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import pro.qyoga.core.clients.ClientsConfig
import pro.qyoga.core.clients.api.ClientsService
import pro.qyoga.core.programs.exercises.ExercisesConfig
import pro.qyoga.core.programs.exercises.api.ExercisesService


@Import(ClientsConfig::class, ExercisesConfig::class)
@TestConfiguration
class BackgroundsConfig(
    private val clientsService: ClientsService,
    private val exercisesService: ExercisesService
) {

    @Bean
    fun backgrounds() = Backgrounds(
        ClientsBackgrounds(clientsService),
        ExerciseBackgrounds(exercisesService)
    )

}