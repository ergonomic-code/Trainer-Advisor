package pro.qyoga.core.appointments.core

import org.intellij.lang.annotations.Language
import org.springframework.data.domain.PageRequest
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.data.util.TypeInformation
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.postgresql.toPGInterval
import pro.azhidkov.platform.spring.sdj.erpo.ErgoRepository
import pro.azhidkov.platform.spring.sdj.sortBy
import pro.qyoga.core.users.therapists.TherapistRef
import java.sql.Timestamp
import java.time.Duration
import java.time.Instant
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
    BasicPersistentEntity(TypeInformation.of(Appointment::class.java)),
    jdbcConverter,
    relationalMappingContext
) {

    object Page {
        val lastTenByDate = PageRequest.of(0, 10, sortBy(Appointment::dateTime).descending())
    }

}

fun AppointmentsRepo.findAllFutureAppointments(
    therapist: TherapistRef,
    now: Instant,
    currentUserTimeZone: ZoneId
): Collection<Appointment> {
    val query = """
        SELECT *
        FROM appointments
        WHERE
            therapist_ref = :therapist AND
            date_trunc('day', date_time AT TIME ZONE :currentUserTimeZone) >= date_trunc('day', :now AT TIME ZONE :currentUserTimeZone)
        ORDER BY date_time
    """.trimIndent()

    val params: Map<String, Any?> = mapOf(
        "therapist" to therapist.id,
        "now" to Timestamp.from(now),
        "currentUserTimeZone" to currentUserTimeZone.id
    )
    return this.findAll(query, params, Appointment.Fetch.summaryRefs)
}

fun AppointmentsRepo.findPastAppointmentsSlice(
    therapist: TherapistRef,
    now: Instant,
    currentUserTimeZone: ZoneId
): Collection<Appointment> {
    val query = """
        SELECT *
        FROM appointments
        WHERE
            therapist_ref = :therapist AND
            date_trunc('day', date_time AT TIME ZONE :currentUserTimeZone) < date_trunc('day', :now AT TIME ZONE :currentUserTimeZone)
    """.trimIndent()

    val params: Map<String, Any?> = mapOf(
        "therapist" to therapist.id,
        "now" to Timestamp.from(now),
        "currentUserTimeZone" to currentUserTimeZone.id
    )
    return this.findSlice(query, params, AppointmentsRepo.Page.lastTenByDate, Appointment.Fetch.summaryRefs)
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