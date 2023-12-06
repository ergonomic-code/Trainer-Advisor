package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component


@Component
data class Backgrounds(
    val clients: ClientsBackgrounds,
    val exercises: ExerciseBackgrounds,
    val users: UsersBackgrounds
)