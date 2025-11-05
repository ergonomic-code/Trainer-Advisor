package pro.qyoga.tests.fixture.test_apis

import org.springframework.stereotype.Component
import pro.qyoga.core.appointments.notifications.fill_schedule.FillScheduleNotificationsSettings
import pro.qyoga.core.appointments.notifications.fill_schedule.FillScheduleNotificationsSettingsRepo
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.object_mothers.pushes.web.WebPushesObjectMother.aWebPushSubscription
import java.time.DayOfWeek
import java.time.LocalTime


@Component
class NotificationsTestApi(
    private val webPushesTestApi: WebPushesTestApi,
    private val fillScheduleNotificationsSettingsRepo: FillScheduleNotificationsSettingsRepo
) {

    fun isFillScheduleNotificationsEnabled(therapistRef: TherapistRef): Boolean {
        return fillScheduleNotificationsSettingsRepo.findForTherapist(therapistRef)?.enabled ?: false
    }

    fun createFillScheduleSettings(
        therapist: TherapistRef,
        notificationDay: DayOfWeek,
        notificationTime: LocalTime,
        enabled: Boolean = true
    ) {
        fillScheduleNotificationsSettingsRepo.createIfNotExists(
            FillScheduleNotificationsSettings(
                id = therapist,
                enabled = enabled,
                dayOfWeek = notificationDay,
                scheduledTime = notificationTime
            )
        )
    }

    fun enableNotifications(therapist: TherapistRef) {
        webPushesTestApi.createSubscription(therapist, aWebPushSubscription())
    }

}
