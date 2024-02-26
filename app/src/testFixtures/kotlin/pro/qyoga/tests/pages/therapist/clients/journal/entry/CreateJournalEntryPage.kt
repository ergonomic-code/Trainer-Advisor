package pro.qyoga.tests.pages.therapist.clients.journal.entry

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.platform.html.Component


object CreateJournalEntryPage : Component {

    const val PATH = "/therapist/clients/{id}/journal/create"

    override fun selector() = "#createJournalEntryTabContent"

    override fun matcher(): Matcher<Element> = haveComponent(CreateJournalEntryForm)

}