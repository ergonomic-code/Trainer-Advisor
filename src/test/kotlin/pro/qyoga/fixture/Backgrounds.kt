package pro.qyoga.fixture

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.ui.ExtendedModelMap
import pro.qyoga.app.therapist.clients.ClientListPageController
import pro.qyoga.app.therapist.programs.exercises.ExercisesListPageController
import pro.qyoga.core.clients.api.ClientCardDto
import pro.qyoga.core.clients.api.ClientsService
import pro.qyoga.core.clients.internal.Client
import pro.qyoga.core.programs.exercises.api.CreateExerciseRequest
import pro.qyoga.core.programs.exercises.api.ExerciseDto
import pro.qyoga.core.programs.exercises.api.ExerciseSearchDto
import pro.qyoga.core.programs.exercises.api.ExercisesService
import pro.qyoga.fixture.clients.ClientsObjectMother
import pro.qyoga.fixture.therapists.THE_THERAPIST_ID


@Component
data class Backgrounds(
    val clients: ClientsBackgrounds,
    val exercises: ExerciseBackgrounds
)

@Component
class ClientsBackgrounds(
    private val clientsService: ClientsService,
    private val clientListPageController: ClientListPageController
) {

    fun createClients(clients: List<ClientCardDto>, therapistId: Long = THE_THERAPIST_ID): Iterable<Client> {
        return clientsService.createClients(therapistId, clients)
    }

    fun createClients(count: Int, therapistId: Long = THE_THERAPIST_ID) {
        clientsService.createClients(therapistId, ClientsObjectMother.createClientCardDtos(count))
    }

    fun getAllClients(): Page<Client> {
        val model = ExtendedModelMap()
        clientListPageController.getClients(Pageable.ofSize(Int.MAX_VALUE), model)
        return ClientListPageController.getClients(model)
    }

}

@Component
class ExerciseBackgrounds(
    private val exercisesService: ExercisesService,
    private val exercisesListPageController: ExercisesListPageController
) {

    fun createExercises(exercises: List<CreateExerciseRequest>) {
        exercisesService.addExercises(exercises.map { it to emptyMap() }, THE_THERAPIST_ID)
    }

    fun findExercise(exerciseSearchDto: ExerciseSearchDto): ExerciseDto? {
        val model = ExtendedModelMap()
        exercisesListPageController.getExercisesFiltered(exerciseSearchDto, Pageable.ofSize(2), model)
        val page = ExercisesListPageController.getExercises(model)
        check(page.content.size <= 1)
        return page.content.firstOrNull()
    }

}