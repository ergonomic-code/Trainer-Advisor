package pro.qyoga.core.appointments.notifications.fill_schedule

import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.java.sql.get
import pro.azhidkov.platform.java.time.Interval
import pro.qyoga.core.users.therapists.Therapist
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*


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

    fun findTherapistsToNotify(dayOfWeek: DayOfWeek, notificationInterval: Interval<LocalTime>): List<TherapistRef> {
        val query = """
            SELECT settings.id as therapist_id
            FROM therapist_fill_schedule_notifications_settings settings
                     JOIN therapist_time_zones tz ON tz.id = settings.id
            WHERE 
                enabled = true
                AND day_of_week = :dayOfWeek
                  -- В общем случае результат перевода локального времени в UTC зависит от даты (например, действует ли летнее время)
                  -- поэтому для приведения локального времени напоминания к UTC его надо привязать к текущей дате
                  AND (
                    CASE (:startTime::time <= :endTime::time)
                        WHEN true THEN 
                            timezone(tz.time_zone, CURRENT_DATE + settings.scheduled_time) 
                                BETWEEN timezone('UTC', CURRENT_DATE + :startTime::time) 
                                    AND timezone('UTC', CURRENT_DATE + :endTime::time)
                        WHEN false THEN 
                            timezone(tz.time_zone, CURRENT_DATE + settings.scheduled_time) >= timezone('UTC', CURRENT_DATE + :startTime::time) 
                                OR timezone(tz.time_zone, CURRENT_DATE + settings.scheduled_time) <= timezone('UTC', CURRENT_DATE + :endTime::time)
                    END
                  )
        """.trimIndent()

        return jdbcClient.sql(query)
            .param("dayOfWeek", dayOfWeek.name)
            .param("startTime", notificationInterval.from)
            .param("endTime", notificationInterval.to)
            .query { rs, _ -> TherapistRef.to<Therapist, UUID>(rs[1]) }
            .list()
    }

}
