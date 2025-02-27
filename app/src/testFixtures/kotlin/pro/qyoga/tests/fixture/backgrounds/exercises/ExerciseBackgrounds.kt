package pro.qyoga.tests.fixture.backgrounds.exercises

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import pro.azhidkov.platform.file_storage.api.StoredFile
import pro.qyoga.app.therapist.therapy.exercises.ExercisesListPageController
import pro.qyoga.core.therapy.exercises.ExercisesService
import pro.qyoga.core.therapy.exercises.dtos.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSearchDto
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.exercises.model.Exercise
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.ExercisesObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.ImagesGenerationMode
import pro.qyoga.tests.fixture.object_mothers.therapy.exercises.None

@Component
class ExerciseBackgrounds(
    private val exercisesService: ExercisesService,
    private val exercisesListPageController: ExercisesListPageController
) {

    fun createExercises(
        exercises: List<CreateExerciseRequest>,
        images: List<Map<Int, StoredFile>> = emptyList()
    ): Iterable<Exercise> {
        require(images.isEmpty() || images.size == exercises.size) {
            "Список изображений должен быть либо пустым, либо иметь длину, равную длине списка упражнений"
        }

        val exerciseImagesMap: Iterable<Map<Int, StoredFile>> = (images.takeIf { it.isNotEmpty() }
            ?: exercises.map { emptyMap() })

        val exercisesWithImages = exercises.zip(exerciseImagesMap)

        return exercisesService.addExercises(exercisesWithImages, THE_THERAPIST_ID)
    }

    fun findExerciseSummary(exerciseSearchDto: ExerciseSearchDto): ExerciseSummaryDto? {
        val model =
            exercisesListPageController.getExercisesFiltered(
                exerciseSearchDto,
                Pageable.ofSize(2),
                theTherapistUserDetails
            )
        val page = model.exercises
        check(page.content.size <= 1)
        return page.content.firstOrNull()
    }

    fun getExerciseStepImage(exerciseId: Long, stepIdx: Int): ByteArray? {
        return exercisesService.getStepImage(exerciseId, stepIdx)
            ?.inputStream
            .use {
                it?.readAllBytes()
            }
    }

    fun findExercise(exerciseId: Long): Exercise =
        exercisesService.findById(exerciseId)!!

    fun createExercises(
        count: Int,
        eachExerciseStepsCount: Int,
        imagesGenerationMode: ImagesGenerationMode,
        ownerRef: TherapistRef = THE_THERAPIST_REF
    ): Iterable<Exercise> {
        return (1..count).map { createExercise(eachExerciseStepsCount, imagesGenerationMode, ownerRef) }
    }

    fun createExercise(
        stepsCount: Int = 0,
        imagesGenerationMode: ImagesGenerationMode = None,
        ownerRef: TherapistRef = THE_THERAPIST_REF
    ): Exercise {
        val steps = ExercisesObjectMother.exerciseSteps(stepsCount)
        val createExerciseRequest = ExercisesObjectMother.createExerciseRequest { steps }
        val images = imagesGenerationMode.generateImages(stepsCount)

        return exercisesService.addExercise(createExerciseRequest, images, ownerRef)
    }

}