package pro.qyoga.tests.clients.pages.therapist.clients.journal.list

import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.jsoup.nodes.Element
import pro.qyoga.core.clients.journals.api.JournalEntry
import pro.qyoga.core.formats.russianDateFormat
import pro.qyoga.platform.spring.sdj.AggregateReferenceTarget
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.clients.pages.therapist.clients.journal.entry.CreateJournalEntryPage
import pro.qyoga.tests.clients.pages.therapist.clients.journal.entry.EditJournalEntryPage
import pro.qyoga.tests.infra.html.Link


object ClientJournalFragment {

    private val addEntryLink = Link.hxGet("addEntryLink", CreateJournalEntryPage.PATH, "Добавить запись")
    private val editEntryLink = Link.hxGet("editEntryLink", EditJournalEntryPage.PATH, "")

    fun fragmentFor(entries: List<JournalEntry>): PageMatcher = object : PageMatcher {

        override fun match(element: Element) {
            element shouldHaveComponent addEntryLink

            val entryElements = element.select("div.journalEntry")
            entryElements shouldHaveSize entries.size

            entries.zip(entryElements).forAll { (entry, el) ->

                val expectedEditLink = editEntryLink.urlPattern
                    .replace("{clientId}", entry.client.id.toString())
                    .replace("{entryId}", entry.id.toString())
                el.select(".editEntryLink").attr(editEntryLink.targetAttr) shouldBe expectedEditLink

                el.select(".entryDate").text() shouldBe russianDateFormat.format(entry.date)
                el.select(".entryTherapeuticTask")
                    .text() shouldContain (entry.therapeuticTask as AggregateReferenceTarget).entity.name
                el.select(".entryText").text() shouldBe entry.entryText
            }
        }

    }

}