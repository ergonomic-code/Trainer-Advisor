package pro.qyoga.app.therapist

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.app.therapist.clients.ClientListPageController
import pro.qyoga.app.therapist.programs.exercises.CreateExercisePageController
import pro.qyoga.app.therapist.programs.exercises.ExercisesListPageController
import pro.qyoga.core.clients.ClientsConfig
import pro.qyoga.core.programs.exercises.ExercisesConfig


@Import(ClientsConfig::class, ExercisesConfig::class)
@Configuration
class TherapistWebAppConfig(
    private val clientsConfig: ClientsConfig,
    private val exercisesConfig: ExercisesConfig
) {

    @Bean
    fun clientsListPageController() =
        ClientListPageController(clientsConfig.clientsService())


    @Bean
    fun exercisesListPageController() =
        ExercisesListPageController(exercisesConfig.exercisesService())

    @Bean
    fun createExercisePageController() =
        CreateExercisePageController(exercisesConfig.exercisesService())

}