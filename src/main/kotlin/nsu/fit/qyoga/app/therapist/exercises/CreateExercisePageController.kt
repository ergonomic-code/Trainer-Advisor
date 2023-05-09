package nsu.fit.qyoga.app.therapist.exercises

import nsu.fit.platform.lang.toDurationMinutes
import nsu.fit.platform.spring.hxRedirect
import nsu.fit.qyoga.core.exercises.api.ExercisesService
import nsu.fit.qyoga.core.exercises.api.model.Exercise
import nsu.fit.qyoga.core.exercises.api.model.ExercisePurpose
import nsu.fit.qyoga.core.therapeutic_purposes.api.TherapeuticPurpose
import nsu.fit.qyoga.core.therapeutic_purposes.api.TherapeuticPurposesService
import nsu.fit.qyoga.core.users.internal.QyogaUserDetails
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

data class CreateExerciseRequest(
    val title: String,
    val description: String,
    val indications: String,
    val contradictions: String,
    val purposes: String,
    val duration: Double,
    val exerciseTypeId: Long
)

@Controller
@RequestMapping("/exercises/create")
class CreateExercisePageController(
    private val exercisesService: ExercisesService,
    private val therapeuticPurposesService: TherapeuticPurposesService
) {

    @GetMapping
    fun getCreateExercisePage(): String {
        return "therapist/exercises/exercise-create"
    }

    @PostMapping
    fun createExercise(
        @ModelAttribute createExerciseRequest: CreateExerciseRequest,
        @AuthenticationPrincipal principal: QyogaUserDetails,
    ): ResponseEntity<Unit> {
        val chosenPurposes = createExerciseRequest.purposes.split(",")
            .map { TherapeuticPurpose(it.trim()) }
        val persistedPurposes = therapeuticPurposesService.saveAll(chosenPurposes)
        val exercisePurposes = persistedPurposes.map { ExercisePurpose(AggregateReference.to(it.id)) }
            .toSet()

        exercisesService.addExercise(
            Exercise(
                createExerciseRequest.title,
                createExerciseRequest.description,
                createExerciseRequest.indications,
                createExerciseRequest.contradictions,
                createExerciseRequest.duration.toDurationMinutes(),
                createExerciseRequest.exerciseTypeId,
                principal.id,
                exercisePurposes
            )
        )

        return hxRedirect("/exercises")
    }

}
