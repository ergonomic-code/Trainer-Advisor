package pro.qyoga.i9ns.pushes.web

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.pushes.web.model.WebPushSubscription


@Repository
class WebPushSubscriptionsRepo(
    private val jdbcClient: JdbcClient
) {

    private val webPushSubscriptionRowMapper = RowMapper { rs, _ ->
        WebPushSubscription(
            rs.getString("endpoint"),
            rs.getTimestamp("expiration_time")?.time,
            WebPushSubscription.Keys(
                rs.getString("p256dh"),
                rs.getString("auth")
            )
        )
    }

    fun addSubscription(therapistRef: TherapistRef, subscription: WebPushSubscription) {
        val query = """
            INSERT INTO therapist_web_push_subscriptions (therapist_ref, p256dh, auth, endpoint, expiration_time)
            VALUES (:therapistRef::uuid, :p256dh, :auth, :endpoint, :expirationTime)
            ON CONFLICT (therapist_ref, p256dh) 
            DO UPDATE SET 
                endpoint = excluded.endpoint, 
                auth = excluded.auth, 
                expiration_time = excluded.expiration_time,
                updated_at = CURRENT_TIMESTAMP, 
                version = therapist_web_push_subscriptions.version + 1
        """.trimIndent()

        jdbcClient.sql(query)
            .param("therapistRef", therapistRef.id.toString())
            .param("p256dh", subscription.keys.p256dh)
            .param("auth", subscription.keys.auth)
            .param("endpoint", subscription.endpoint)
            .param("expirationTime", subscription.expirationTime)
            .update()
    }

    fun findTherapistSubscriptions(therapistRef: TherapistRef): List<WebPushSubscription> {
        val query = """
            SELECT * FROM therapist_web_push_subscriptions
            WHERE therapist_ref = :therapistRef::uuid
        """.trimIndent()

        return jdbcClient.sql(query)
            .param("therapistRef", therapistRef.id.toString())
            .query(webPushSubscriptionRowMapper)
            .list()
    }

    fun deleteSubscription(therapistRef: TherapistRef, p256dh: String) {
        val query = """
            DELETE FROM therapist_web_push_subscriptions
            WHERE therapist_ref = :therapistRef::uuid AND p256dh = :p256dh
        """.trimIndent()

        jdbcClient.sql(query)
            .param("therapistRef", therapistRef.id.toString())
            .param("p256dh", p256dh)
            .update()
    }

}
