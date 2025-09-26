package pro.qyoga.tests.pages.therapist.appointments

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element
import pro.qyoga.app.therapist.appointments.core.schedule.GoogleCalendarSettingsController
import pro.qyoga.tests.assertions.haveAttributeValue
import pro.qyoga.tests.platform.html.Component
import pro.qyoga.tests.platform.kotest.all


object GoogleCalendarSettingsLoaderComponent : Component {

    override fun selector(): String = "#google-calendar-settings-container"

    override fun matcher(): Matcher<Element> =
        Matcher.all(
            haveAttributeValue("hx-get", GoogleCalendarSettingsController.PATH)
        )

}