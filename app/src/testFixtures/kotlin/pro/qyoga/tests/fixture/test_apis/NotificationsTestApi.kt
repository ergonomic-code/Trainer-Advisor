package pro.qyoga.tests.fixture.test_apis

import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.stereotype.Component
import pro.qyoga.core.appointments.notifications.fill_schedule.FillScheduleNotificationsSettings
import pro.qyoga.core.appointments.notifications.fill_schedule.FillScheduleNotificationsSettingsRepo
import pro.qyoga.core.users.therapists.Therapist
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*


@Component
class NotificationsTestApi(
    private val fillScheduleNotificationsSettingsRepo: FillScheduleNotificationsSettingsRepo
) {

    fun isFillScheduleNotificationsEnabled(therapistRef: TherapistRef): Boolean {
        return fillScheduleNotificationsSettingsRepo.findForTherapist(therapistRef)?.enabled ?: false
    }

    fun createFillScheduleSettings(
        moscowTherapist: AggregateReference<Therapist, UUID>,
        notificationDay: DayOfWeek,
        notificationTime: LocalTime,
        enabled: Boolean = true
    ) {
        fillScheduleNotificationsSettingsRepo.createIfNotExists(
            FillScheduleNotificationsSettings(
                id = moscowTherapist,
                enabled = enabled,
                dayOfWeek = notificationDay,
                scheduledTime = notificationTime
            )
        )
    }

}
