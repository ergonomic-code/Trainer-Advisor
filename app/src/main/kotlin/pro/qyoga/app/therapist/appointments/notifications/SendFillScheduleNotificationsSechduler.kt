package pro.qyoga.app.therapist.appointments.notifications

import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant


private const val windowSizeProp = $$"${trainer-advisor.appointments.notifications.fill-schedule.window-size}"

@Component
class SendFillScheduleNotificationsScheduler(
    private val sendFillScheduleNotificationsOp: SendFillScheduleNotificationsOp,
    @Value(windowSizeProp) private val windowSize: Duration
) {

    @Scheduled(fixedRateString = windowSizeProp)
    fun sendFillScheduleNotifications() {
        sendFillScheduleNotificationsOp(Instant.now(), windowSize)
    }

}
