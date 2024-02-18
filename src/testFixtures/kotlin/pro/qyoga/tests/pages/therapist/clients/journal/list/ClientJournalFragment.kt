package pro.qyoga.tests.pages.therapist.clients.journal.list

import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.jsoup.nodes.Element
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.l10n.russianDateFormat
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.pages.therapist.clients.journal.entry.CreateJournalEntryPage
import pro.qyoga.tests.pages.therapist.clients.journal.entry.EditJournalEntryPage
import pro.qyoga.tests.platform.html.Link


object ClientJournalFragment {

    private val addEntryLink = Link.hxGet("addEntryLink", CreateJournalEntryPage.PATH, "Добавить запись")
    private val editEntryLink = Link.hxGet("editEntryLink", EditJournalEntryPage.PATH, "")
    private val deleteEntryLink = Link.hxDelete("deleteEntryLink", EditJournalEntryPage.PATH, "")

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

                val expectedDeleteLink = deleteEntryLink.urlPattern
                    .replace("{clientId}", entry.client.id.toString())
                    .replace("{entryId}", entry.id.toString())
                el.select(".deleteEntryLink").attr(deleteEntryLink.targetAttr) shouldBe expectedDeleteLink

                el.select(".entryDate").text() shouldBe russianDateFormat.format(entry.date)
                el.select(".entryTherapeuticTask").text() shouldContain entry.therapeuticTask.resolveOrThrow().name
                el.select(".entryText").text() shouldBe entry.entryText
            }
        }

    }

}