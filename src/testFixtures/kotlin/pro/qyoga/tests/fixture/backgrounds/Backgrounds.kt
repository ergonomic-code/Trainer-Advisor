package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import pro.qyoga.tests.fixture.backgrounds.exercises.ExerciseBackgrounds


@Component
data class Backgrounds(
    val clients: ClientsBackgrounds,
    val clientJournal: ClientJournalBackgrounds,
    val exercises: ExerciseBackgrounds,
    val therapeuticTasks: TherapeuticTasksBackgrounds,
    val users: UsersBackgrounds,
    val spring: SpringBackgrounds
)