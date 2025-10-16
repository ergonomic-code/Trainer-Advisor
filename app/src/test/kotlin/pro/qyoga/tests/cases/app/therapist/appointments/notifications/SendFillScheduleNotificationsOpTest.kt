package pro.qyoga.tests.cases.app.therapist.appointments.notifications

import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.qyoga.app.therapist.appointments.notifications.SendFillScheduleNotificationsOp
import pro.qyoga.tests.fixture.data.asiaNovosibirskTimeZone
import pro.qyoga.tests.fixture.object_mothers.pushes.web.WebPushesObjectMother.aWebPushSubscription
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.test_apis.NotificationsTestApi
import pro.qyoga.tests.fixture.test_apis.WebPushesTestApi
import pro.qyoga.tests.fixture.wiremocks.MockWebPushServer
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import java.time.*


@DisplayName("Операция отправки уведомлений о заполнении расписания")
class SendFillScheduleNotificationsOpTest : QYogaAppIntegrationBaseTest() {

    private val notificationsTestApi = getBean<NotificationsTestApi>()
    private val sendFillScheduleNotificationsOp = getBean<SendFillScheduleNotificationsOp>()
    private val webPushesTestApi = getBean<WebPushesTestApi>()
    private val mockWebPushServer = getBean<MockWebPushServer>()

    @Test
    fun `при наличии терапевта с настроенными уведомлениями на момент вызова операции и зарегестированной подпиской должна отправлять ему уведомление`() {
        // Given
        val notificationInstant = ZonedDateTime.of(LocalDateTime.of(2025, 10, 13, 10, 0), asiaNovosibirskTimeZone)
            .toInstant()
        notificationsTestApi.createFillScheduleSettings(
            THE_THERAPIST_REF,
            DayOfWeek.MONDAY,
            LocalTime.of(10, 0)
        )
        val endpoint = mockWebPushServer.aEndpoint()
        webPushesTestApi.createSubscription(THE_THERAPIST_REF, aWebPushSubscription(endpoint = endpoint))

        mockWebPushServer.OnSendPush(endpoint).returnsOk()

        // When
        sendFillScheduleNotificationsOp(notificationInstant, Duration.ofMinutes(10))

        // Then
        val sentPushes = mockWebPushServer.getSentPushes()
        sentPushes shouldHaveSize 1
    }

}
