package pro.qyoga.tests.pages.therapist.clients.journal.entry

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.tests.assertions.PageMatcher


object TherapeuticTasksSearchResult {

    const val PATH = "/therapist/therapeutic-tasks/autocomplete-search"

    fun componentFor(expectedSearchResult: List<TherapeuticTask>): PageMatcher = object : PageMatcher {

        override fun match(element: Element) {
            val options = element.select("option")
            options shouldHaveSize expectedSearchResult.size

            options.zip(expectedSearchResult).forEach { (element, task) ->
                element.`val`() shouldBe task.name
            }
        }

    }

}