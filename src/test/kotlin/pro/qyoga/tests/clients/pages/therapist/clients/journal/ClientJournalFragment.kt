package pro.qyoga.tests.clients.pages.therapist.clients.journal

import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.jsoup.nodes.Element
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.formats.russianDateFormat
import pro.qyoga.platform.spring.sdj.AggregateReferenceTarget
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.infra.html.Link


object ClientJournalFragment {

    private val addEntryLink = Link.hxGet("addEntryLink", CreateJournalEntryPage.path, "Добавить запись")

    fun fragmentFor(entries: List<JournalEntry>): PageMatcher = object : PageMatcher {

        override fun match(element: Element) {
            element shouldHave addEntryLink

            val entryElements = element.select("div.journalEntry")
            entryElements shouldHaveSize entries.size

            entries.zip(entryElements).forAll { (entry, el) ->
                el.select(".entryDate").text() shouldBe russianDateFormat.format(entry.date)
                el.select(".entryTherapeuticTask")
                    .text() shouldContain (entry.therapeuticTask as AggregateReferenceTarget).entity.name
                el.select(".entryText").text() shouldBe entry.entryText
            }
        }

    }

}