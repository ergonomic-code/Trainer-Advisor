package pro.qyoga.tests.fixture.test_apis

import org.springframework.stereotype.Component
import pro.qyoga.core.appointments.notifications.fill_schedule.FillScheduleNotificationsSettingsRepo
import pro.qyoga.core.users.therapists.TherapistRef


@Component
class NotificationsTestApi(
    private val fillScheduleNotificationsSettingsRepo: FillScheduleNotificationsSettingsRepo
) {

    fun isFillScheduleNotificationsEnabled(therapistRef: TherapistRef): Boolean {
        return fillScheduleNotificationsSettingsRepo.findForTherapist(therapistRef)?.enabled ?: false
    }

}
