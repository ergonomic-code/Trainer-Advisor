package pro.qyoga.core.appointments.notifications.fill_schedule

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.DayOfWeek
import java.time.LocalTime


@Table("therapist_fill_schedule_notifications_settings")
class FillScheduleNotificationsSettings(
    @Id val id: TherapistRef,
    val enabled: Boolean,
    val dayOfWeek: DayOfWeek,
    val scheduledTime: LocalTime
) {

    companion object {

        fun defaultSettingsFor(therapistRef: TherapistRef) = FillScheduleNotificationsSettings(
            id = therapistRef,
            enabled = true,
            dayOfWeek = DayOfWeek.MONDAY,
            scheduledTime = LocalTime.of(10, 0)
        )

    }

}
