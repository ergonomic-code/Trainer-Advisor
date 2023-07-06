package nsu.fit.qyoga.cases.core.exercises.internal

import io.kotest.matchers.collections.shouldHaveSize
import nsu.fit.qyoga.app.therapist.exercises.CreateExercisePageController
import nsu.fit.qyoga.app.therapist.exercises.ExercisesListPageController
import nsu.fit.qyoga.cases.core.exercises.ExercisesTestConfig
import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.dtos.CreateExerciseRequest
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.users.UsersConfig
import nsu.fit.qyoga.core.users.api.UserService
import nsu.fit.qyoga.core.users.internal.QyogaUserDetails
import nsu.fit.qyoga.infra.QYogaModuleBaseTest
import nsu.fit.qyoga.infra.TestContainerDbContextInitializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.test.context.ContextConfiguration
import org.springframework.ui.ExtendedModelMap


@ContextConfiguration(
    classes = [ExercisesTestConfig::class, UsersConfig::class],
    initializers = [TestContainerDbContextInitializer::class]
)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class ExercisesControllerTest(
    @Autowired private val exercisesService: ExercisesService,
    @Autowired private val userService: UserService
) : QYogaModuleBaseTest() {

    private val createExercisePageController = CreateExercisePageController(exercisesService)
    private val exercisesListPageController = ExercisesListPageController(exercisesService)


    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/exercises-init-script.sql" to "dataSource",
        )
    }

    @Test
    fun `Therapeutic purposes amount should not cause duplicated exercise creation`() {
        // Given
        val therapist = userService.findByUsername("therapist")!!

        // When
        val createExerciseRequest = CreateExerciseRequest(
            "irrelevant",
            "irrelevant",
            "irrelevant",
            "irrelevant",
            0.0,
            1,
            emptyList()
        )
        createExercisePageController.createExercise(
            createExerciseRequest = createExerciseRequest,
            imagesMap = emptyMap(),
            purposes = "purpose1,purpose2",
            principal = QyogaUserDetails(therapist.id, "irrelevant", "irrelevant", emptyList())
        )

        // Then
        val model = ExtendedModelMap()
        exercisesListPageController.getExercises(ExerciseSearchDto(), pageSize = 2, pageNumber = 1, model)
        (model["exercises"] as Page<*>).content shouldHaveSize 1
    }

}