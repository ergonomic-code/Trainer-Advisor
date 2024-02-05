package pro.qyoga.core.appointments.core

import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.data.util.TypeInformation
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.spring.sdj.erpo.ErgoRepository
import pro.qyoga.core.appointments.core.model.Appointment
import pro.qyoga.core.users.therapists.TherapistRef
import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneId

@Repository
class AppointmentsRepo(
    internal val jdbcAggregateTemplate: JdbcAggregateOperations,
    internal val namedParameterJdbcOperations: NamedParameterJdbcOperations,
    jdbcConverter: JdbcConverter,
    relationalMappingContext: RelationalMappingContext,
) : ErgoRepository<Appointment, Long>(
    jdbcAggregateTemplate,
    namedParameterJdbcOperations,
    BasicPersistentEntity(TypeInformation.of(Appointment::class.java)),
    jdbcConverter,
    relationalMappingContext
)

fun AppointmentsRepo.findAllFutureAppointments(
    therapist: TherapistRef,
    now: Instant,
    currentUserTimeZone: ZoneId
): Iterable<Appointment> {
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
    return this.findAll(query, params, listOf(Appointment::clientRef, Appointment::typeRef))
}
