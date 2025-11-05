package pro.qyoga.app.therapist.appointments.core.schedule.settings

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView
import pro.qyoga.core.appointments.notifications.fill_schedule.FillScheduleNotificationsSettings
import pro.qyoga.core.appointments.notifications.fill_schedule.FillScheduleNotificationsSettingsRepo
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref
import pro.qyoga.l10n.russianTimeFormat
import pro.qyoga.l10n.systemLocale
import java.time.format.TextStyle


data class NotificationsSettingsModel(
    val settings: FillScheduleNotificationsSettings
) : ModelAndView(
    "therapist/appointments/notifications-settings-component",
    mapOf(
        "fillScheduleRemainderDay" to settings.dayOfWeek.getDisplayName(TextStyle.FULL, systemLocale),
        "fillScheduleRemainderTime" to settings.scheduledTime.format(russianTimeFormat)
    )
)

@Controller
class NotificationsSettingsController(
    private val fillScheduleNotificationsSettingsRepo: FillScheduleNotificationsSettingsRepo
) {

    @GetMapping(PATH)
    fun getNotificationsSettingsComponent(
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): NotificationsSettingsModel {
        val settings = fillScheduleNotificationsSettingsRepo.findForTherapist(therapist.ref)
            ?: FillScheduleNotificationsSettings.defaultSettingsFor(therapist.ref)
        return NotificationsSettingsModel(settings)
    }

    companion object {
        const val PATH = "/therapist/schedule/settings/notifications"
    }

}
