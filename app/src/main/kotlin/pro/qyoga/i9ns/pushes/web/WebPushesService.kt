package pro.qyoga.i9ns.pushes.web

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.pushes.web.model.WebPush


@Component
class WebPushesService(
    private val webPushSubscriptionsRepo: WebPushSubscriptionsRepo,
    private val webPushServiceClient: WebPushServiceClient,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun sendPush(therapistRef: TherapistRef, push: WebPush) {
        val webPushSubscriptions = webPushSubscriptionsRepo.findTherapistSubscriptions(therapistRef)

        webPushSubscriptions.forEach { therapistSubscription ->
            try {
                webPushServiceClient.sendPush(therapistSubscription.subscription, push)
            } catch (e: Exception) {
                log.warn("Fill schedule push notification sending failed", e)
            }
        }
    }

}
