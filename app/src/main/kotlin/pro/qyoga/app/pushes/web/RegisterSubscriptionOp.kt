package pro.qyoga.app.pushes.web

import org.springframework.stereotype.Component
import pro.qyoga.core.appointments.notifications.fill_schedule.FillScheduleNotificationsSettings
import pro.qyoga.core.appointments.notifications.fill_schedule.FillScheduleNotificationsSettingsRepo
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.pushes.web.WebPushSubscriptionsRepo
import pro.qyoga.i9ns.pushes.web.model.WebPushSubscription


@Component
class RegisterSubscriptionOp(
    private val webPushSubscriptionsRepo: WebPushSubscriptionsRepo,
    private val fillScheduleNotificationsSettingsRepo: FillScheduleNotificationsSettingsRepo
) : (TherapistRef, WebPushSubscription) -> Unit {

    override fun invoke(
        therapistRef: TherapistRef,
        subscription: WebPushSubscription,
    ) {
        webPushSubscriptionsRepo.addSubscription(therapistRef, subscription)
        fillScheduleNotificationsSettingsRepo.createIfNotExists(
            FillScheduleNotificationsSettings.defaultSettings(
                therapistRef
            )
        )
    }

}
