package pro.qyoga.tests.pages.therapist.appointments

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.platform.html.Component
import pro.qyoga.tests.platform.html.Link
import pro.qyoga.tests.platform.kotest.all


object GoogleCalendarSettingsComponent : Component {

    private val connectButton =
        Link("connect-google-calendar", "/oauth2/authorization/google", "Подключить Google Calendar")

    override fun selector(): String =
        "#google-calendar-settings"

    override fun matcher(): Matcher<Element> {
        return Matcher.all(
            haveComponent(connectButton)
        )
    }

}