package pro.qyoga.tests.fixture.test_apis

import org.springframework.stereotype.Component
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.pushes.web.WebPushSubscriptionsRepo
import pro.qyoga.i9ns.pushes.web.model.TherapistWebPushSubscription
import pro.qyoga.i9ns.pushes.web.model.WebPushSubscription


@Component
class WebPushesTestApi(
    private val webPushSubscriptionsRepo: WebPushSubscriptionsRepo
) {

    fun createSubscription(therapistRef: TherapistRef, webPushSubscription: WebPushSubscription) {
        webPushSubscriptionsRepo.addSubscription(TherapistWebPushSubscription(therapistRef, webPushSubscription))
    }

    fun getTherapistSubscriptions(therapistRef: TherapistRef): List<WebPushSubscription> {
        return webPushSubscriptionsRepo.findTherapistsSubscriptions(listOf(therapistRef))
            .map { it.subscription }
    }

}
