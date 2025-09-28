package pro.qyoga.tests.pages.therapist.appointments

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element
import pro.qyoga.i9ns.calendars.google.views.GoogleAccountCalendarsSettingsView
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.platform.html.Component
import pro.qyoga.tests.platform.html.Link
import pro.qyoga.tests.platform.kotest.all


class GoogleCalendarSettingsComponent(
    private val accounts: List<GoogleAccountCalendarsSettingsView>
) : Component {

    private val connectButton =
        Link("connect-google-calendar", "/oauth2/authorization/google", "Добавить аккаунт")

    override fun selector(): String =
        "#google-calendar-settings"

    override fun matcher(): Matcher<Element> {
        return Matcher.all(
            haveComponent(connectButton),
            *(accounts.map { haveComponent(GoogleAccountComponent()) }.toTypedArray())
        )
    }

    class GoogleAccountComponent : Component {

        override fun selector(): String =
            ".google-account-item"

        override fun matcher(): Matcher<Element> {
            return haveComponent(".google-account-error-content")
        }

    }

}
