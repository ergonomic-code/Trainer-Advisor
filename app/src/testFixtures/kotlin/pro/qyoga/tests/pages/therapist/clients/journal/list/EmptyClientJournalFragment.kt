package pro.qyoga.tests.pages.therapist.clients.journal.list

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element
import pro.qyoga.app.therapist.clients.journal.edit_entry.create.CreateJournalEntryPageController
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.assertions.haveText
import pro.qyoga.tests.platform.html.Component
import pro.qyoga.tests.platform.html.Link
import pro.qyoga.tests.platform.kotest.all

object EmptyClientJournalFragment : Component {

    private val addFirstEntryLink =
        Link.hxGet("addFirstEntryLink", CreateJournalEntryPageController.CREATE_JOURNAL_PAGE_URL, "Добавьте первую")

    override fun selector(): String = "#emptyJournal"

    override fun matcher(): Matcher<Element> {
        return Matcher.all(
            haveText("Вы не добавили ещё ни одной записи. Добавьте первую"),
            haveComponent(addFirstEntryLink)
        )
    }

}