package pro.qyoga.core.appointments.core

import org.intellij.lang.annotations.Language
import org.springframework.core.convert.support.DefaultConversionService
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.postgresql.toPGInterval
import pro.azhidkov.platform.spring.sdj.PGIntervalToDurationConverter
import pro.azhidkov.platform.spring.sdj.ergo.ErgoRepository
import pro.qyoga.core.users.therapists.TherapistRef
import java.sql.Timestamp
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Repository
class AppointmentsRepo(
    jdbcAggregateTemplate: JdbcAggregateOperations,
    namedParameterJdbcOperations: NamedParameterJdbcOperations,
    jdbcConverter: JdbcConverter,
    relationalMappingContext: RelationalMappingContext,
) : ErgoRepository<Appointment, Long>(
    jdbcAggregateTemplate,
    namedParameterJdbcOperations,
    Appointment::class,
    jdbcConverter,
    relationalMappingContext
)

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

private val localizedAppointmentSummaryRowMapper =
    DataClassRowMapper(LocalizedAppointmentSummary::class.java).apply<DataClassRowMapper<LocalizedAppointmentSummary>> {
        conversionService = DefaultConversionService().apply {
            addConverter(PGIntervalToDurationConverter())
        }
    }

fun AppointmentsRepo.findAllByInterval(
    therapist: TherapistRef,
    from: LocalDate,
    to: LocalDate,
    currentUserTimeZone: ZoneId
): Iterable<LocalizedAppointmentSummary> {
    @Language("PostgreSQL") val query = """
        WITH localized_appointment_summary AS
         (SELECT  
                 a.id,
                 c.last_name || ' ' || c.first_name || 
                     CASE WHEN length(c.middle_name) > 0 THEN ' ' || c.middle_name ELSE '' END AS client_name,
                 at.name type_name,
                 tt.name therapeutic_task_name,
                 a.date_time AT TIME ZONE :localTimeZone AS date_time,
                 a.duration,
                 a.status,
                 a.therapist_ref
          FROM appointments a
                   LEFT JOIN public.clients c ON c.id = a.client_ref
                   LEFT JOIN appointment_types at ON a.type_ref = at.id
                   LEFT JOIN public.therapeutic_tasks tt on a.therapeutic_task_ref = tt.id)
        SELECT *
        FROM localized_appointment_summary
        WHERE therapist_ref = :therapist
          AND date_trunc('day', date_time) >= :from
          AND date_trunc('day', date_time) <= :to
        ORDER BY date_time
    """

    val params = mapOf(
        "therapist" to therapist.id,
        "from" to from.atStartOfDay(),
        "to" to to.atStartOfDay(),
        "localTimeZone" to currentUserTimeZone.id
    )
    return findAll(query, params, localizedAppointmentSummaryRowMapper)
}