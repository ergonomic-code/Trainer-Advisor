package pro.qyoga.core.appointments.notifications.fill_schedule

import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import pro.qyoga.core.users.therapists.TherapistRef


@Repository
class FillScheduleNotificationsSettingsRepo(
    private val jdbcClient: JdbcClient,
    private val jdbcAggregateTemplate: JdbcAggregateOperations
) {

    fun createIfNotExists(fillScheduleNotificationsSettings: FillScheduleNotificationsSettings) {
        val query = """
            INSERT INTO therapist_fill_schedule_notifications_settings (id, enabled, day_of_week, scheduled_time)
            VALUES (:id, :enabled, :day_of_week, :time)
            ON CONFLICT (id) DO NOTHING
        """.trimIndent()

        jdbcClient.sql(query)
            .param("id", fillScheduleNotificationsSettings.id.id)
            .param("enabled", fillScheduleNotificationsSettings.enabled)
            .param("day_of_week", fillScheduleNotificationsSettings.dayOfWeek.name)
            .param("time", fillScheduleNotificationsSettings.scheduledTime)
            .update()
    }

    fun findForTherapist(therapistRef: TherapistRef): FillScheduleNotificationsSettings? {
        return jdbcAggregateTemplate.findById(therapistRef, FillScheduleNotificationsSettings::class.java)
    }

}
