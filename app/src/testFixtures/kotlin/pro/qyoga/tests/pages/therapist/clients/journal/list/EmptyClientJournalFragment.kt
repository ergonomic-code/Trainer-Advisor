package pro.qyoga.tests.pages.therapist.clients.journal.list

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.haveText
import pro.qyoga.tests.platform.html.Component

object EmptyClientJournalFragment : Component {

    override fun selector(): String = "#emptyJournal"

    override fun matcher(): Matcher<Element> {
        return haveText("Вы не добавили ещё ни одной записи. Добавьте первую")
    }
}