package pro.qyoga.app.therapist.therapy.exercises

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import pro.qyoga.core.therapy.exercises.api.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.api.ExercisesService
import pro.qyoga.core.users.internal.QyogaUserDetails
import pro.qyoga.infra.images.api.Image
import pro.qyoga.platform.spring.http.hxRedirect

@Controller
@RequestMapping("/therapist/exercises/create")
class CreateExercisePageController(
    private val exercisesService: ExercisesService
) {

    @GetMapping
    fun getCreateExercisePage(): String {
        return "therapist/exercises/exercise-create"
    }

    @PostMapping
    fun createExercise(
        @ModelAttribute createExerciseRequest: CreateExerciseRequest,
        @RequestParam imagesMap: Map<String, MultipartFile>,
        @AuthenticationPrincipal principal: QyogaUserDetails,
    ): ResponseEntity<Unit> {
        val stepImages = imagesMap
            .map { (partName, img) ->
                partName.toStepIdx() to Image(img.originalFilename!!, img.contentType!!, img.size, img.bytes)
            }.toMap()

        exercisesService.addExercise(createExerciseRequest, stepImages, principal.id)

        return hxRedirect("/therapist/exercises")
    }

    private fun String.toStepIdx() = substring("stepImage".length).toInt()

}
