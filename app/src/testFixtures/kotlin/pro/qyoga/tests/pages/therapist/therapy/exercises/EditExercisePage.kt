package pro.qyoga.tests.pages.therapist.therapy.exercises

import io.kotest.matchers.Matcher
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.core.therapy.exercises.model.Exercise
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.alwaysSuccess
import pro.qyoga.tests.assertions.shouldBeElement
import pro.qyoga.tests.platform.html.Component

object EditExercisePage : Component {

    const val PATH = "/therapist/exercises/{exerciseId}"

    override fun selector() = "#exerciseFrom"

    override fun matcher(): Matcher<Element> = alwaysSuccess()

    fun pageFor(exercise: Exercise): PageMatcher = PageMatcher { element ->
        element.select("title")[0].text() shouldBe exercise.title

        element.getElementById(EditExerciseForm.id)!! shouldBeElement EditExerciseForm.exerciseForm(exercise)
    }

}