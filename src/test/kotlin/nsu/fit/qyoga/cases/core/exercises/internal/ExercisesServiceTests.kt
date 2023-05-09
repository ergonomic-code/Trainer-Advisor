package nsu.fit.qyoga.cases.core.exercises.internal

import io.kotest.matchers.shouldBe
import nsu.fit.qyoga.cases.core.exercises.ExercisesTestConfig
import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.infra.QYogaModuleBaseTest
import nsu.fit.qyoga.infra.TestContainerDbContextInitializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ContextConfiguration
import java.time.Duration

@ContextConfiguration(
    classes = [ExercisesTestConfig::class],
    initializers = [TestContainerDbContextInitializer::class]
)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class ExercisesServiceTests(
    @Autowired private val exercisesService: ExercisesService
) : QYogaModuleBaseTest() {

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/exercises-init-script.sql" to "dataSource",
            "/db/migration/demo/V23050903__insert_exercises_data.sql" to "dataSource",
        )
    }

    @Test
    fun `QYoga can retrieve exercises without filters`() {
        // Given
        val searchDto = ExerciseSearchDto()

        // When
        val exercises = exercisesService.getExercises(
            searchDto,
            PageRequest.of(0, 10)
        )

        // Then
        exercises.content.size shouldBe 5
        exercises.totalElements shouldBe 5
        exercises.content.map { it.id.toInt() }.sorted() shouldBe listOf(1, 2, 3, 4, 5)
    }

    @Test
    fun `QYoga can retrieve exercises with filters`() {
        // Given
        val searchDto = ExerciseSearchDto(title = "Разминка")

        // When
        val exercises = exercisesService.getExercises(
            searchDto,
            PageRequest.of(0, 10)
        )

        // Then
        exercises.content.size shouldBe 1
        exercises.totalElements shouldBe 1
        exercises.content.map { it.title }[0].startsWith("Разминка")
        exercises.content.map { it.id } shouldBe listOf(1)
    }

    @Test
    fun `QYoga shouldn't retrieve exercises with invalid filter`() {
        // Given
        val searchDto = ExerciseSearchDto(title = "====")

        // When
        val exercises = exercisesService.getExercises(
            searchDto,
            PageRequest.of(0, 10)
        )

        // Then
        exercises.content.size shouldBe 0
        exercises.totalElements shouldBe 0
    }

    @Test
    fun `QYoga can retrieve exercises with type filter`() {
        // Given
        val searchDto = ExerciseSearchDto(exerciseTypeId = 1)
        val expectedExercise = Exercise(
            title = "Разминка для шеи",
            description = "",
            indications = "",
            contradictions = "",
            duration = Duration.ofMinutes(10),
            exerciseTypeId = 1,
            therapistId = 1,
            setOf(),
            id = 1
        )

        // When
        val exercises = exercisesService.getExercises(
            searchDto,
            PageRequest.of(0, 10)
        )

        // Then
        exercises.totalElements shouldBe 1
        exercises.content[0].title shouldBe expectedExercise.title
        exercises.content[0].duration shouldBe expectedExercise.duration
        exercises.content[0].type.id shouldBe expectedExercise.exerciseTypeId
    }
}
