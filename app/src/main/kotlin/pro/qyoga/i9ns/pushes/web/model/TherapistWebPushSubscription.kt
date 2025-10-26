package pro.qyoga.i9ns.pushes.web.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.uuid.UUIDv7
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.Instant
import java.util.*


@Table("therapist_web_push_subscriptions")
data class TherapistWebPushSubscription(
    val therapistRef: TherapistRef,
    val subscription: WebPushSubscription,

    @Id val id: UUID = UUIDv7.randomUUID(),
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant? = null,
    val version: Long = 0
) {

    @Transient
    val p256dh = subscription.keys.p256dh

}
