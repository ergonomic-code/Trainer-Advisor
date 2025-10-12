package pro.qyoga.tests.fixture.test_apis

import org.springframework.stereotype.Component
import pro.qyoga.app.pushes.web.WebPushesController
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.pushes.web.WebPushSubscription
import pro.qyoga.i9ns.pushes.web.WebPushSubscriptionsRepo
import pro.qyoga.tests.fixture.object_mothers.therapists.idOnlyUserDetails


@Component
class WebPushesTestApi(
    private val webPushesController: WebPushesController,
    private val webPushSubscriptionsRepo: WebPushSubscriptionsRepo
) {

    fun createSubscription(therapistRef: TherapistRef, webPushSubscription: WebPushSubscription) {
        webPushesController.createSubscription(webPushSubscription, idOnlyUserDetails(therapistRef.id))
    }

    fun getTherapistSubscriptions(therapistRef: TherapistRef): List<WebPushSubscription> {
        return webPushSubscriptionsRepo.findTherapistSubscriptions(therapistRef)
    }

}
