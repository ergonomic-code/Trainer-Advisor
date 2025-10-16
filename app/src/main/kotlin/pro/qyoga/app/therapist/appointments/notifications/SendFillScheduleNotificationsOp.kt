package pro.qyoga.app.therapist.appointments.notifications

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import pro.azhidkov.platform.java.time.Interval
import pro.qyoga.app.therapist.appointments.core.schedule.GetCalendarAppointmentsOp
import pro.qyoga.core.appointments.notifications.fill_schedule.FillScheduleNotificationsSettingsRepo
import pro.qyoga.i9ns.pushes.web.WebPushesService
import pro.qyoga.i9ns.pushes.web.model.WebPush
import java.net.URI
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset


val fillScheduleWebPush = WebPush(
    title = "Не забудьте заполнить расписание",
    body = "Открыть приложение",
    deepLink = URI.create(GetCalendarAppointmentsOp.PATH),
    topic = "fill-schedule"
)

@Component
class SendFillScheduleNotificationsOp(
    private val fillScheduleNotificationsSettingsRepo: FillScheduleNotificationsSettingsRepo,
    private val webPushesService: WebPushesService
) : (Instant, Duration) -> Unit {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun invoke(currentTime: Instant, windowSize: Duration) {
        log.info("Sending fill schedule notifications, windowSize={}", windowSize)

        // тут есть баг - если у терапевта настроены уведомления на понедельник в 02:55 мск (23:55 UTC)
        // то при окне в 10 минут и времени срабатывания на 4-ой минуте это уведомление не будет отправлено
        // потому что dayOfWeek будет уже вторником
        // Но такие условия в реально мире маловероятны, поэтому польза фикса не перевешивает пользы

        val dayOfWeek = currentTime.atOffset(ZoneOffset.UTC).dayOfWeek
        val time = currentTime.atOffset(ZoneOffset.UTC).toLocalTime() - windowSize
        val therapistsToNotify = fillScheduleNotificationsSettingsRepo.findTherapistsToNotify(
            dayOfWeek = dayOfWeek,
            notificationInterval = Interval.of(time, windowSize)
        )

        therapistsToNotify.forEach {
            webPushesService.sendPush(it, fillScheduleWebPush)
        }
    }

}
