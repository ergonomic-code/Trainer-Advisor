package pro.qyoga.fixture

import org.springframework.data.domain.Pageable
import pro.qyoga.core.clients.api.ClientDto
import pro.qyoga.core.clients.api.ClientsCrudService
import pro.qyoga.core.programs.exercises.api.CreateExerciseRequest
import pro.qyoga.core.programs.exercises.api.ExerciseDto
import pro.qyoga.core.programs.exercises.api.ExerciseSearchDto
import pro.qyoga.core.programs.exercises.api.ExercisesService
import pro.qyoga.fixture.clients.ClientDtoObjectMother
import pro.qyoga.fixture.therapists.THE_THERAPIST_ID


data class Backgrounds(
    val clients: ClientsBackgrounds,
    val exercises: ExerciseBackgrounds
)

class ClientsBackgrounds(
    private val clientsCrudService: ClientsCrudService
) {

    fun createClients(clients: List<ClientDto>) {
        clientsCrudService.saveAll(clients)
    }

    fun createClients(count: Int) {
        clientsCrudService.saveAll(ClientDtoObjectMother.createClientDtos(count))
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