package pro.qyoga.fixture

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import pro.qyoga.core.clients.api.ClientSearchDto
import pro.qyoga.core.clients.api.ClientsService
import pro.qyoga.core.clients.api.CreateClientRequest
import pro.qyoga.core.clients.internal.Client
import pro.qyoga.core.programs.exercises.api.CreateExerciseRequest
import pro.qyoga.core.programs.exercises.api.ExerciseDto
import pro.qyoga.core.programs.exercises.api.ExerciseSearchDto
import pro.qyoga.core.programs.exercises.api.ExercisesService
import pro.qyoga.fixture.clients.ClientsObjectMother
import pro.qyoga.fixture.therapists.THE_THERAPIST_ID


data class Backgrounds(
    val clients: ClientsBackgrounds,
    val exercises: ExerciseBackgrounds
)

class ClientsBackgrounds(
    private val clientsService: ClientsService
) {

    fun createClients(clients: List<CreateClientRequest>, therapistId: Long = THE_THERAPIST_ID) {
        clientsService.createClients(therapistId, clients)
    }

    fun createClients(count: Int, therapistId: Long = THE_THERAPIST_ID) {
        clientsService.createClients(therapistId, ClientsObjectMother.createClientRequests(count))
    }

    fun getAllClients(): Page<Client> {
        return clientsService.findClients(ClientSearchDto.ALL, Pageable.ofSize(Int.MAX_VALUE))
    }

}

class ExerciseBackgrounds(
    private val exercisesService: ExercisesService
) {

    fun createExercises(exercises: List<CreateExerciseRequest>) {
        exercisesService.addExercises(exercises.map { it to emptyMap() }, THE_THERAPIST_ID)
    }

    fun findExercise(exerciseSearchDto: ExerciseSearchDto): ExerciseDto? {
        val page = exercisesService.findExercises(exerciseSearchDto, Pageable.ofSize(2))
        check(page.content.size <= 1)
        return page.content.firstOrNull()
    }

}