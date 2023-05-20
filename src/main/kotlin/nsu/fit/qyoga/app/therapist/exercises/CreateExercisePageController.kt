package nsu.fit.qyoga.app.therapist.exercises

import nsu.fit.platform.spring.hxRedirect
import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.dtos.CreateExerciseRequest
import nsu.fit.qyoga.core.images.api.model.Image
import nsu.fit.qyoga.core.therapeutic_purposes.api.TherapeuticPurpose
import nsu.fit.qyoga.core.users.internal.QyogaUserDetails
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

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
            @RequestParam purposes: String,
            @AuthenticationPrincipal principal: QyogaUserDetails,
    ): ResponseEntity<Unit> {
        val chosenPurposes = purposes.split(",")
                .map { TherapeuticPurpose(it.trim()) }

        val stepImages = imagesMap
                .map { (partName, img) ->
                    partName.toStepIdx() to Image(
                            img.originalFilename!!,
                            img.contentType!!,
                            img.size,
                            img.bytes
                    )
                }.toMap()

        exercisesService.addExercise(createExerciseRequest, stepImages, principal.id, chosenPurposes)

        return hxRedirect("/therapist/exercises")
    }

    private fun String.toStepIdx() = substring("stepImage".length).toInt()

}
