package pro.qyoga.core.appointments.core

import org.intellij.lang.annotations.Language
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.java.time.Interval
import pro.azhidkov.platform.java.time.zoneId
import pro.azhidkov.platform.postgresql.toPGInterval
import pro.azhidkov.platform.spring.jdbc.taDataClassRowMapper
import pro.azhidkov.platform.spring.sdj.ergo.ErgoRepository
import pro.qyoga.core.appointments.core.model.Appointment
import pro.qyoga.core.appointments.core.views.LocalizedAppointmentSummary
import pro.qyoga.core.calendar.api.CalendarItem
import pro.qyoga.core.calendar.api.CalendarsService
import pro.qyoga.core.calendar.api.SearchResult
import pro.qyoga.core.users.therapists.TherapistRef
import java.sql.Timestamp
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*

@Repository
class AppointmentsRepo(
    jdbcAggregateTemplate: JdbcAggregateOperations,
    namedParameterJdbcOperations: NamedParameterJdbcOperations,
    jdbcConverter: JdbcConverter,
    relationalMappingContext: RelationalMappingContext,
) : ErgoRepository<Appointment, UUID>(
    jdbcAggregateTemplate,
    namedParameterJdbcOperations,
    Appointment::class,
    jdbcConverter,
    relationalMappingContext
), CalendarsService<UUID> {

    override fun findCalendarItemsInInterval(
        therapist: TherapistRef,
        interval: Interval<ZonedDateTime>,
    ): SearchResult<UUID> {
        @Language("PostgreSQL") val query = """
        WITH localized_appointment_summary AS
         (SELECT  
                 a.id,
                 c.last_name || ' ' || c.first_name || 
                     CASE WHEN length(c.middle_name) > 0 THEN ' ' || c.middle_name ELSE '' END AS client_name,
                 at.name type_name,
                 a.date_time AT TIME ZONE :localTimeZone AS date_time,
                 a.duration,
                 a.status,
                 a.therapist_ref
          FROM appointments a
                   LEFT JOIN public.clients c ON c.id = a.client_ref
                   LEFT JOIN appointment_types at ON a.type_ref = at.id)
                   
        SELECT *
        FROM localized_appointment_summary
        WHERE therapist_ref = :therapist
          AND date_trunc('day', date_time) >= :from
          AND date_trunc('day', date_time) <= :to
        ORDER BY date_time
    """

        val params = mapOf(
            "therapist" to therapist.id,
            "from" to interval.from.toLocalDateTime(),
            "to" to interval.to.toLocalDateTime(),
            "localTimeZone" to interval.zoneId.id
        )
        return SearchResult(findAll(query, params, localizedAppointmentSummaryRowMapper))
    }

    override fun findById(
        therapistRef: TherapistRef,
        eventId: UUID
    ): CalendarItem<UUID, ZonedDateTime>? {
        TODO()
    }

}

fun AppointmentsRepo.findIntersectingAppointment(
    therapist: TherapistRef,
    startInstant: Instant,
    duration: Duration
): Appointment? {
    @Language("PostgreSQL") val query = """
        SELECT * FROM appointments
        WHERE
            therapist_ref = :therapist AND
            (date_time, duration) OVERLAPS (:startInstant, :duration)
    """.trimIndent()

    val params = mapOf(
        "therapist" to therapist.id,
        "startInstant" to Timestamp.from(startInstant),
        "duration" to duration.toPGInterval()
    )

    return findOne(query, params, Appointment.Fetch.summaryRefs)
}

private val localizedAppointmentSummaryRowMapper = taDataClassRowMapper<LocalizedAppointmentSummary>()
