package pro.qyoga.app.therapist.therapy.exercises

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import pro.azhidkov.platform.file_storage.api.StoredFile
import pro.azhidkov.platform.spring.http.hxRedirect
import pro.qyoga.app.platform.toStoredFile
import pro.qyoga.core.therapy.exercises.ExercisesService
import pro.qyoga.core.therapy.exercises.dtos.CreateExerciseRequest
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails

@Controller
@RequestMapping("/therapist/exercises/create")
class CreateExercisePageController(
    private val exercisesService: ExercisesService
) {

    @GetMapping
    fun getCreateExercisePage(): String {
        return "therapist/therapy/exercises/exercise-create"
    }

    @PostMapping
    fun createExercise(
        createExerciseRequest: CreateExerciseRequest,
        @RequestParam imagesMap: Map<String, MultipartFile>,
        @AuthenticationPrincipal principal: QyogaUserDetails,
    ): ResponseEntity<Unit> {
        val stepImages: Map<Int, StoredFile> = imagesMap
            .map { (partName: String, img: MultipartFile) ->
                partName.toStepIdx() to img.toStoredFile()
            }
            .toMap()

        exercisesService.addExercise(createExerciseRequest, stepImages, principal.id)

        return hxRedirect("/therapist/exercises")
    }

}

private fun String.toStepIdx() = substring("stepImage".length).toInt()

fun Int.toStepIdx() = "stepImage$this"