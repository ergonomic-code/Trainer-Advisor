package pro.qyoga.i9ns.pushes.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.spring.sdj.query.query
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.pushes.web.model.TherapistWebPushSubscription


@Repository
class WebPushSubscriptionsRepo(
    private val jdbcClient: JdbcClient,
    private val jdbcAggregateTemplate: JdbcAggregateTemplate,
    private val objectMapper: ObjectMapper,
) {

    fun addSubscription(therapistSubscription: TherapistWebPushSubscription) {
        val query = """
            INSERT INTO therapist_web_push_subscriptions (id, therapist_ref, subscription)
            VALUES (:id::uuid, :therapistRef::uuid, :subscription::jsonb)
            ON CONFLICT (therapist_ref, p256dh) 
            DO UPDATE SET 
                subscription = excluded.subscription, 
                updated_at = CURRENT_TIMESTAMP, 
                version = therapist_web_push_subscriptions.version + 1
        """.trimIndent()

        jdbcClient.sql(query)
            .param("id", therapistSubscription.id)
            .param("therapistRef", therapistSubscription.therapistRef.id.toString())
            .param("subscription", objectMapper.writeValueAsString(therapistSubscription.subscription))
            .update()
    }

    fun findTherapistsSubscriptions(therapistRefs: Iterable<TherapistRef>): List<TherapistWebPushSubscription> {
        if (therapistRefs.count() == 0) {
            return emptyList()
        }
        val query = query {
            TherapistWebPushSubscription::therapistRef `in` therapistRefs.map { it.id }
        }

        return jdbcAggregateTemplate.findAll(query, TherapistWebPushSubscription::class.java)
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
