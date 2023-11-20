package pro.qyoga.fixture

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import pro.qyoga.core.clients.ClientsConfig
import pro.qyoga.core.clients.api.ClientsCrudService
import pro.qyoga.core.programs.exercises.api.ExercisesService


@Import(ClientsConfig::class)
@TestConfiguration
class BackgroundsConfig(
    private val clientsService: ClientsCrudService,
    private val exercisesService: ExercisesService
) {

    @Bean
    fun backgrounds() = Backgrounds(
        ClientsBackgrounds(clientsService),
        ExerciseBackgrounds(exercisesService)
    )

}