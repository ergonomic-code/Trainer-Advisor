package pro.qyoga.tests.pages.therapist.appointments.google_calendars

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Element
import pro.qyoga.i9ns.calendars.google.views.GoogleCalendarSettingsView
import pro.qyoga.tests.assertions.haveCheckboxChecked
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.platform.html.Component


class GoogleCalendarSettingsComponent(
    private val googleCalendarsSettingsView: GoogleCalendarSettingsView
) : Component {

    override fun selector(): String = "#${googleCalendarsSettingsView.id}"

    override fun matcher(): Matcher<Element> =
        Matcher.all(
            haveComponent(".calendar-name:containsOwn(${googleCalendarsSettingsView.title})"),
            haveCheckboxChecked(".should-be-shown-toggle", googleCalendarsSettingsView.shouldBeShown)
        )

}
