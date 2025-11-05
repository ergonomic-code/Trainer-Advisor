package pro.qyoga.tests.pages.therapist.appointments.notifications

import io.kotest.matchers.shouldBe
import pro.qyoga.core.appointments.notifications.fill_schedule.FillScheduleNotificationsSettings
import pro.qyoga.l10n.russianTimeFormat
import pro.qyoga.l10n.systemLocale
import pro.qyoga.tests.assertions.ComponentMatcher
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.platform.html.TextElement
import java.time.format.TextStyle

object NotificationsSettings {

    private val remainderDayLabel = TextElement("#fill-sched-remainder-day")
    private val remainderTimeLabel = TextElement("#fill-sched-remainder-time")

    fun with(settings: FillScheduleNotificationsSettings) =
        ComponentMatcher.Companion("#notifications-settings") {
            it shouldHave remainderDayLabel
            remainderDayLabel.text(it) shouldBe settings.dayOfWeek.getDisplayName(TextStyle.FULL, systemLocale)

            it shouldHave remainderTimeLabel
            remainderTimeLabel.text(it) shouldBe settings.scheduledTime.format(russianTimeFormat)
        }

}
