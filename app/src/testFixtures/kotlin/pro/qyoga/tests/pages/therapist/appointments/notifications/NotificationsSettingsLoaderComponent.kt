package pro.qyoga.tests.pages.therapist.appointments.notifications

import io.kotest.matchers.Matcher
import org.jsoup.nodes.Element
import pro.qyoga.app.therapist.appointments.core.schedule.settings.NotificationsSettingsController
import pro.qyoga.tests.assertions.haveAttributeValue
import pro.qyoga.tests.platform.html.Component
import pro.qyoga.tests.platform.kotest.all


object NotificationsSettingsLoaderComponent : Component {

    override fun selector(): String = "#notifications-settings-container"

    override fun matcher(): Matcher<Element> =
        Matcher.all(
            haveAttributeValue("hx-get", NotificationsSettingsController.PATH)
        )

}
