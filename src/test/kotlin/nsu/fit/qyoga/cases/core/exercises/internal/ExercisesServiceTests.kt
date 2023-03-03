package nsu.fit.qyoga.cases.core.exercises.internal

import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.shouldBe
import nsu.fit.qyoga.cases.core.exercises.ExercisesTestConfig
import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import nsu.fit.qyoga.infra.QYogaModuleBaseTest
import nsu.fit.qyoga.infra.TestContainerDbContextInitializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(
    classes = [ExercisesTestConfig::class],
    initializers = [TestContainerDbContextInitializer::class]
)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ActiveProfiles("test")
class ExercisesServiceTests(
    @Autowired private val exercisesService: ExercisesService
) : QYogaModuleBaseTest() {

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/exercises-init-script.sql" to "dataSource",
            "/db/exercises-insert-data-script.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can retrieve exercises without filters`() {
        //Given
        val searchDto = ExerciseSearchDto()

        //When
        val exercises = exercisesService.getExercises(
            searchDto,
            PageRequest.of(0, 10)
        )

        //Then
        exercises.content.size shouldBe 5
        exercises.totalElements shouldBe 5
        exercises.content.map { it.id.toInt() }.sorted() shouldBe listOf(1, 2, 3, 4, 5)
    }

    @Test
    fun `QYoga can retrieve exercises with filters`() {
        //Given
        val searchDto = ExerciseSearchDto(title = "Разминка")

        //When
        val exercises = exercisesService.getExercises(
            searchDto,
            PageRequest.of(0, 10)
        )

        //Then
        exercises.content.size shouldBe 1
        exercises.totalElements shouldBe 1
        exercises.content.map { it.title }[0].startsWith("Разминка")
        exercises.content.map { it.id } shouldBe listOf(1)
    }

    @Test
    fun `QYoga shouldn't retrieve exercises with invalid filter`() {
        //Given
        val searchDto = ExerciseSearchDto(title = "====")

        //When
        val exercises = exercisesService.getExercises(
            searchDto,
            PageRequest.of(0, 10)
        )

        //Then
        exercises.content.size shouldBe 0
        exercises.totalElements shouldBe 0
    }

    @Test
    fun `QYoga can retrieve exercises with type filter`() {
        //Given
        val searchDto = ExerciseSearchDto(exerciseType = ExerciseType.WarmUp)

        //When
        val exercises = exercisesService.getExercises(
            searchDto,
            PageRequest.of(0, 10)
        )

        //Then
        exercises.totalElements shouldBe 1
        exercises.content[0].title shouldBe "Разминка для шеи"
        exercises.content[0].duration shouldBe "00:10:00"
    }

    @Test
    fun `QYoga can find exercise by duration`() {
        // Given
        val requiredDuration = "00:04:00"
        val searchDto = ExerciseSearchDto(duration = requiredDuration)

        // When
        val exercises = exercisesService.getExercises(searchDto, PageRequest.of(0, 10))

        // Then
        exercises shouldHaveAtLeastSize 1
        exercises.content.forAll { it.duration shouldBe requiredDuration }
    }

}