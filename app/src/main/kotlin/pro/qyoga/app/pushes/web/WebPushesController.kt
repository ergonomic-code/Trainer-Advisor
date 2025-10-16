package pro.qyoga.app.pushes.web

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref
import pro.qyoga.i9ns.pushes.web.WebPushSubscriptionsRepo
import pro.qyoga.i9ns.pushes.web.model.WebPushSubscription


@RestController
class WebPushesController(
    private val webPushSubscriptionsRepo: WebPushSubscriptionsRepo,
    private val registerSubscriptionOp: RegisterSubscriptionOp
) {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(PATH)
    fun createSubscription(
        @RequestBody @Valid subscription: WebPushSubscription,
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ) {
        registerSubscriptionOp(therapist.ref, subscription)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(DELETE_SUBSCRIPTION_PATH)
    fun deleteSubscription(
        @PathVariable p256dh: String,
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ) {
        webPushSubscriptionsRepo.deleteSubscription(therapist.ref, p256dh)
    }

    companion object {
        const val PATH = "/pushes/web/subscriptions"
        const val DELETE_SUBSCRIPTION_PATH = "$PATH/{p256dh}"
    }

}
