package pro.qyoga.tests.pages.therapist.clients.journal.list

import io.kotest.inspectors.forAll
import io.kotest.matchers.Matcher
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.jsoup.nodes.Element
import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.qyoga.app.therapist.clients.journal.edit_entry.create.CreateJournalEntryPageController
import pro.qyoga.app.therapist.clients.journal.list.JournalPageController
import pro.qyoga.core.clients.journals.model.JournalEntry
import pro.qyoga.l10n.russianDateFormat
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.haveAttributeValue
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.assertions.shouldNotHave
import pro.qyoga.tests.pages.therapist.clients.journal.entry.EditJournalEntryPage
import pro.qyoga.tests.platform.html.Component
import pro.qyoga.tests.platform.html.Link


object ClientJournalFragment {

    private val addEntryLink =
        Link.hxGet("addEntryLink", CreateJournalEntryPageController.CREATE_JOURNAL_PAGE_URL, "Добавить запись")

    fun fragmentFor(entries: List<JournalEntry>, hasMore: Boolean = false): PageMatcher = PageMatcher { element ->
        element shouldHaveComponent addEntryLink

        ClientJournalEntriesFragment.fragmentFor(entries, hasMore).match(element)
    }

}

object ClientJournalEntriesFragment {

    private val editEntryLink = Link.hxGet("editEntryLink", EditJournalEntryPage.PATH, "")
    private val deleteEntryLink = Link.hxDelete("deleteEntryLink", EditJournalEntryPage.PATH, "")
    private const val NEXT_PAGE_LOADER_HANDLE = ".loader"

    fun fragmentFor(entries: List<JournalEntry>, hasMore: Boolean = false): PageMatcher = PageMatcher { element ->
        val entryElements = element.select("div.journalEntry")
        entryElements shouldHaveSize entries.size

        entries.zip(entryElements).forAll { (entry, el) ->

            val expectedEditLink = editEntryLink.urlPattern
                .replace("{clientId}", entry.clientRef.id.toString())
                .replace("{entryId}", entry.id.toString())
            el.select(".editEntryLink").attr(editEntryLink.targetAttr) shouldBe expectedEditLink

            val expectedDeleteLink = deleteEntryLink.urlPattern
                .replace("{clientId}", entry.clientRef.id.toString())
                .replace("{entryId}", entry.id.toString())
            el.select(".deleteEntryLink").attr(deleteEntryLink.targetAttr) shouldBe expectedDeleteLink

            el.select(".entryDate").text() shouldBe russianDateFormat.format(entry.date)
            el.select(".entryTherapeuticTask").text() shouldContain entry.therapeuticTask.resolveOrThrow().name
            el.select(".entryText").text() shouldBe entry.entryText
        }

        if (hasMore) {
            element shouldHaveComponent NextPageLoader(entries.last())
        } else {
            element shouldNotHave NEXT_PAGE_LOADER_HANDLE
        }
    }
}

class NextPageLoader(private val lastEntry: JournalEntry) : Component {

    override fun selector(): String = ".loader"

    override fun matcher(): Matcher<Element> {
        return haveAttributeValue("hx-get", JournalPageController.nextPageUrl(lastEntry))
    }

}