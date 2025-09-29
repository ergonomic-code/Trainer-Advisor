package pro.qyoga.tests.pages.therapist.appointments.google_calendars

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element
import pro.qyoga.i9ns.calendars.google.views.GoogleCalendarsSettingsView
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.platform.html.Component
import pro.qyoga.tests.platform.kotest.all

class GoogleAccountComponent(
    private val googleCalendarsSettingsView: GoogleCalendarsSettingsView
) : Component {

    override fun selector(): String =
        ".google-account-item"

    override fun matcher(): Matcher<Element> {
        return if (googleCalendarsSettingsView.isError) {
            haveComponent(".google-account-error-content")
        } else {
            Matcher.all(
                *googleCalendarsSettingsView.calendars
                    .map { haveComponent(GoogleCalendarSettingsComponent(it)) }
                    .toTypedArray()
            )
        }
    }

}
