package nsu.fit.qyoga.cases.core.exercises.ui

import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.DomElement
import com.gargoylesoftware.htmlunit.html.HtmlPage
import io.kotest.matchers.shouldNotBe
import nsu.fit.qyoga.infra.QYogaAppTestBase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ExerciseViewTest : QYogaAppTestBase() {

    lateinit var webClient: WebClient

    @BeforeEach
    fun init() {
        webClient = WebClient()
    }

    @AfterEach
    fun close() {
        webClient.close()
    }

    @Test
    fun `QYoga returns exercise-search page with table tag`() {
        val page: HtmlPage = webClient.getPage("http://localhost/exercises/")
        page.getElementsById("exercisesTable") shouldNotBe emptyList<DomElement>()
    }

    @Test
    fun `QYoga returns exercise-search page with input fields`() {
        val page: HtmlPage = webClient.getPage("http://localhost/exercises/")
        page.getElementsById("searchExercisesFilterForm") shouldNotBe emptyList<DomElement>()
        page.getElementsById("titleFilter") shouldNotBe emptyList<DomElement>()
        page.getElementsById("contradictionFilter") shouldNotBe emptyList<DomElement>()
        page.getElementsById("therapeuticPurposeFilter") shouldNotBe emptyList<DomElement>()
        page.getElementsById("durationFilter") shouldNotBe emptyList<DomElement>()
        page.getElementsById("exerciseTypeFilter") shouldNotBe emptyList<DomElement>()
    }
}
