package pro.qyoga.tests.cases.core.therapy.programs

import io.kotest.matchers.result.shouldBeSuccess
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.junit.jupiter.api.Test
import pro.azhidkov.platform.file_storage.api.StoredFile
import pro.azhidkov.platform.file_storage.api.StoredFileInputStream
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.qyoga.core.therapy.exercises.model.Exercise
import pro.qyoga.core.therapy.programs.ProgramDocxGenerator
import pro.qyoga.core.therapy.programs.model.DocxExercise
import pro.qyoga.core.therapy.programs.model.DocxProgram
import pro.qyoga.core.therapy.programs.model.DocxStep
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.AllSteps
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.ExercisesObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapy.programs.ProgramsObjectMother
import pro.qyoga.tests.platform.storeArtifact
import java.io.ByteArrayInputStream
import java.nio.file.Path


class ProgramDocxGeneratorTest {

    @Test
    fun `Generator should create valid docx file`() {
        // Given
        val (program, imagesMap) = givenData()

        // When
        val docx = ProgramDocxGenerator.generateDocx(program) { imagesMap[it] }

        // Then
        val buffer = docx.readAllBytes()
        storeArtifact(Path.of("ProgramDocxGeneratorTest", "program.docx"), ByteArrayInputStream(buffer))

        val openResult = runCatching { XWPFDocument(ByteArrayInputStream(buffer)) }
        openResult.shouldBeSuccess()
    }

    private fun givenData(): Pair<DocxProgram, Map<Long, StoredFileInputStream>> {
        val task = TherapeuticTask(THE_THERAPIST_ID, randomCyrillicWord())
        val exercisesWithImages = ExercisesObjectMother.randomExercises(
            count = 3,
            eachExerciseStepsCount = 3,
            imagesGenerationMode = AllSteps,
            generateIds = true
        )
        val program = ProgramsObjectMother.randomProgram(
            therapeuticTask = task.ref(),
            exercises = exercisesWithImages.map { it.first })

        return DocxProgram(
            program.id, program.title, exercisesWithImages.map { exerciseWithImages ->
                var idx: Int = 0
                DocxExercise(exerciseWithImages.first.id, exerciseWithImages.first.title, exerciseWithImages.first.description, exerciseWithImages.first.steps.map {
                    DocxStep(idx++, it.description, exerciseWithImages.second[idx]!!.id)
                })
            }
        ) to getExerciseStepImagesSource(exercisesWithImages)
    }

    private fun getExerciseStepImagesSource(exercisesWithImages: List<Pair<Exercise, Map<Int, StoredFile>>>) =
        exercisesWithImages.flatMap { (ex, stpImgs) ->
            stpImgs.entries.map { (stp, file) ->
                ex.id to StoredFileInputStream(
                    file.metaData,
                    ByteArrayInputStream(file.content)
                )
            }
        }
            .toMap()

}