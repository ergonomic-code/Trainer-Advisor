package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component


@Component
data class Backgrounds(
    val clients: ClientsBackgrounds,
    val clientJournal: ClientJournalBackgrounds,
    val exercises: ExerciseBackgrounds,
    val therapeuticTasks: TherapeuticTasksBackgrounds,
    val users: UsersBackgrounds
)