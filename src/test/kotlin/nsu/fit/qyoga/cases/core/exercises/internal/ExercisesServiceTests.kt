package nsu.fit.qyoga.cases.core.exercises.internal

import io.kotest.matchers.shouldBe
import nsu.fit.qyoga.cases.core.exercises.ExercisesTestConfig
import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.ImagesService
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseStepDto
import nsu.fit.qyoga.core.exercises.api.dtos.ModifiableExerciseDto
import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import nsu.fit.qyoga.core.exercises.internal.ExerciseStepsRepo
import nsu.fit.qyoga.infra.QYogaModuleBaseTest
import nsu.fit.qyoga.infra.TestContainerDbContextInitializer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.postgresql.util.PGInterval
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
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
    @Autowired private val exercisesService: ExercisesService,
    @Autowired private val imagesService: ImagesService,
    @Autowired private val exerciseStepsRepo: ExerciseStepsRepo
) : QYogaModuleBaseTest() {

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/exercises/exercises-init-script.sql" to "dataSource",
            "/db/exercises/exercises-insert-data-script.sql" to "dataSource"
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
        val searchDto = ExerciseSearchDto(exerciseType = ExerciseType.WarmUp)
        val expectedExercise = Exercise(
            id = 1,
            title = "Разминка для шеи",
            description = "",
            indications = "",
            contradictions = "",
            duration = PGInterval("00:10:00"),
            exerciseTypeId = 1,
            therapistId = 1
        )

        // When
        val exercises = exercisesService.getExercises(
            searchDto,
            PageRequest.of(0, 10)
        )

        // Then
        exercises.totalElements shouldBe 1
        exercises.content[0].title shouldBe expectedExercise.title
        PGInterval(exercises.content[0].duration) shouldBe expectedExercise.duration
        exercises.content[0].type.id shouldBe expectedExercise.exerciseTypeId
    }

    @Test
    fun `QYoga can create new exercises without steps`() {
        // Given
        val createDto = ModifiableExerciseDto(
            title = "Разминка для спины",
            description = "",
            indications = "",
            contradiction = "",
            duration = "00:10:00",
            exerciseType = ExerciseType.WarmUp
        )
        val expectedExercise = Exercise(
            id = 6,
            title = "Разминка для спины",
            description = "",
            indications = "",
            contradictions = "",
            duration = PGInterval("00:10:00"),
            exerciseTypeId = 1,
            therapistId = 1
        )

        // When
        val savedExercise = exercisesService.createExercise(createDto, therapistId = 1)

        // Then
        savedExercise.title shouldBe expectedExercise.title
        savedExercise.exerciseTypeId shouldBe expectedExercise.exerciseTypeId
        savedExercise.duration shouldBe expectedExercise.duration
        savedExercise.therapistId shouldBe expectedExercise.therapistId
    }

    @Test
    fun `QYoga can create new exercise with steps without image`() {
        // Given
        val createDto = ModifiableExerciseDto(
            title = "Разминка для спины",
            description = "",
            indications = "",
            contradiction = "",
            duration = "00:10:00",
            exerciseType = ExerciseType.WarmUp,
            exerciseSteps = mutableListOf(ExerciseStepDto(1, "Step 1", null), ExerciseStepDto(2, "Step 2", null))
        )
        val expectedExercise = Exercise(
            id = 6,
            title = "Разминка для спины",
            description = "",
            indications = "",
            contradictions = "",
            duration = PGInterval("00:10:00"),
            exerciseTypeId = 1,
            therapistId = 1
        )

        // When
        val savedExercise = exercisesService.createExercise(createDto, therapistId = 1)
        val savedSteps = exerciseStepsRepo.findAll()

        // Then
        savedExercise.title shouldBe expectedExercise.title
        savedExercise.exerciseTypeId shouldBe expectedExercise.exerciseTypeId
        savedExercise.duration shouldBe expectedExercise.duration
        savedExercise.therapistId shouldBe expectedExercise.therapistId
        savedSteps.filter { it.exerciseId == savedExercise.id }
            .map { it.description } shouldBe createDto.exerciseSteps.map { it.description }

        savedSteps.filter { it.exerciseId == savedExercise.id }.size shouldBe 2
        savedSteps.filter { it.exerciseId == savedExercise.id }[0]
            .imageId shouldBe savedSteps.filter { it.exerciseId == savedExercise.id }[1].imageId
    }

    @Test
    fun `QYoga can create new exercise with steps with image`() {
        // Given
        val createDto = ModifiableExerciseDto(
            title = "Разминка для спины",
            description = "",
            indications = "",
            contradiction = "",
            duration = "00:10:00",
            exerciseType = ExerciseType.WarmUp,
            exerciseSteps = mutableListOf(
                ExerciseStepDto(1, "Step 1", createExampleMultipartFile()),
                ExerciseStepDto(2, "Step 2", createExampleMultipartFile())
            )
        )
        val expectedExercise = Exercise(
            id = 6,
            title = "Разминка для спины",
            description = "",
            indications = "",
            contradictions = "",
            duration = PGInterval("00:10:00"),
            exerciseTypeId = 1,
            therapistId = 1
        )

        // When
        val savedExercise = exercisesService.createExercise(createDto, therapistId = 1)
        val savedSteps = exerciseStepsRepo.findAll()
        val imagesIds = savedSteps.mapNotNull { it.imageId }
        val savedImages = listOf(imagesService.getImage(imagesIds[0]), imagesService.getImage(imagesIds[1]))

        // Then
        savedExercise.title shouldBe expectedExercise.title
        savedExercise.exerciseTypeId shouldBe expectedExercise.exerciseTypeId
        savedExercise.duration shouldBe expectedExercise.duration
        savedExercise.therapistId shouldBe expectedExercise.therapistId
        savedSteps.filter { it.exerciseId == savedExercise.id }
            .map { it.description } shouldBe createDto.exerciseSteps.map { it.description }

        savedSteps.filter { it.exerciseId == savedExercise.id }.size shouldBe 2
        savedImages.map { it?.size } shouldBe createDto.exerciseSteps.map { it.photo }.map { it?.size }
        savedImages.map { it?.data } shouldBe createDto.exerciseSteps.map { it.photo }.map { it?.bytes }
    }

    @Test
    fun `QYoga can retrieve exercise by id`() {
        // Given
        val expectedExercise = createExpectedExercise()

        // When
        val exercise = exercisesService.getExerciseById(1)

        // Then
        exercise.exerciseType shouldBe expectedExercise.exerciseType
        exercise.description shouldBe expectedExercise.description
        exercise.title shouldBe expectedExercise.title
        exercise.duration shouldBe expectedExercise.duration
        exercise.contradiction shouldBe expectedExercise.contradiction
        exercise.indications shouldBe expectedExercise.indications
        exercise.therapeuticPurpose shouldBe expectedExercise.therapeuticPurpose
        exercise.exerciseSteps.size shouldBe expectedExercise.exerciseSteps.size
    }

    private fun createExampleMultipartFile(): MockMultipartFile {
        val fileResource = ClassPathResource("files/pug.jpg")
        return MockMultipartFile(
            "file",
            fileResource.filename,
            MediaType.MULTIPART_FORM_DATA_VALUE,
            fileResource.inputStream
        )
    }

    private fun createExpectedExercise() = ModifiableExerciseDto(
        title = "Разминка для шеи",
        description = "",
        indications = "",
        contradiction = "",
        duration = "00:10:00",
        exerciseType = ExerciseType.WarmUp,
        therapeuticPurpose = "Снятие гипертонуса с мышц шеи",
        exerciseSteps = mutableListOf(
            ExerciseStepDto(
                1,
                "Встаньте ровно, прижмите пятки к полу, потянитесь макушкой к потолку, почувствуйте как вытягивается позвоночник.",
                null
            ),
            ExerciseStepDto(
                2,
                "На выдохе поворот головы в сторону, на вдохе возврат головы в центр, на следующем выдохе - голова в другую сторону. Прислушивайтесь к ощущениям в мышцах, сравнивайте ощущения справа и слева. По 5 поворотов головы в каждую сторону.",
                null
            ),
            ExerciseStepDto(
                3,
                "На выдохе наклон головы к плечу, на вдохе вытяжение макушки к потолку, на следующем выдохе- наклон к другому плечу. Прислушивайтесь к ощущениям в мышцах, сравнивайте ощущения справа и слева. По 5 наклонов головы в каждую сторону.",
                null
            ),
            ExerciseStepDto(
                4,
                "Положите обе ладони под яремную ямку и выполняйте медленные осознанные вращения головой, после каждого круга меняйте направление вращения. По 3 круга в каждом направлении.",
                null
            ),
            ExerciseStepDto(5, "Разомните массажным мячиком возле стены верхние трапеции и мышцы между лопаток.", null)

        ),
    )
}
