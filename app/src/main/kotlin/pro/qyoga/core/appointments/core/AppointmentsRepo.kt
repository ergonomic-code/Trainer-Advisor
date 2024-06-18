package pro.qyoga.core.appointments.core

import org.intellij.lang.annotations.Language
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.postgresql.toPGInterval
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

fun AppointmentsRepo.findAllByInterval(
    therapist: TherapistRef,
    from: LocalDate,
    to: LocalDate,
    currentUserTimeZone: ZoneId
): Iterable<Appointment> {
    @Language("PostgreSQL") val query = """
        SELECT *
        FROM appointments
        WHERE
            therapist_ref = :therapist AND
            date_trunc('day', date_time AT TIME ZONE :currentUserTimeZone) >= date_trunc('day', :from AT TIME ZONE :currentUserTimeZone) AND
            date_trunc('day', date_time AT TIME ZONE :currentUserTimeZone) <= date_trunc('day', :to AT TIME ZONE :currentUserTimeZone)
        ORDER BY date_time AT TIME ZONE :currentUserTimeZone
    """.trimIndent()

    val params = mapOf(
        "therapist" to therapist.id,
        "from" to from,
        "to" to to,
        "currentUserTimeZone" to currentUserTimeZone.id
    )
    return findAll(query, params, Appointment.Fetch.summaryRefs)
}