package pro.qyoga.core.users.settings

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Component
import pro.qyoga.core.users.auth.model.UserRef
import java.time.ZoneId


@Component
class UserSettingsRepo(
    private val namedParameterJdbcOperations: NamedParameterJdbcOperations
) {

    private val defaultTimeZone = ZoneId.of("Asia/Novosibirsk")

    fun getUserTimeZone(userRef: UserRef): ZoneId {
        // Быстрый вариант решения для МВП

        val query = """
            SELECT time_zone FROM appointments
            WHERE therapist_ref = :therapistId
            ORDER BY created_at
            LIMIT 1
        """.trimIndent()

        val lastAppointmentTimeZone = this.namedParameterJdbcOperations.queryForList(
            query,
            mapOf("therapistId" to userRef.id),
            ZoneId::class.java
        )
        return lastAppointmentTimeZone.firstOrNull() ?: defaultTimeZone
    }

}

