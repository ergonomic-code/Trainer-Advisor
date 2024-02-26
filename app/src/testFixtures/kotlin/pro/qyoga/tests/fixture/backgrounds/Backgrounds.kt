package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import pro.qyoga.tests.fixture.backgrounds.exercises.ExerciseBackgrounds


@Component
data class Backgrounds(
    val appointments: AppointmentsBackgrounds,
    val appointmentTypes: AppointmentTypesBackgrounds,
    val clients: ClientsBackgrounds,
    val clientJournal: ClientJournalBackgrounds,
    val clientFiles: ClientFilesBackgrounds,
    val programs: ProgramsBackgrounds,
    val exercises: ExerciseBackgrounds,
    val therapeuticTasks: TherapeuticTasksBackgrounds,
    val users: UsersBackgrounds,
    val spring: SpringBackgrounds
)