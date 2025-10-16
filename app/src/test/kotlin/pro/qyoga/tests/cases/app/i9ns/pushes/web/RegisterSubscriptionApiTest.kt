package pro.qyoga.tests.cases.app.i9ns.pushes.web

import io.kotest.inspectors.forAll
import io.kotest.inspectors.forAny
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.qyoga.tests.clients.api.TrainerAdvisorApis
import pro.qyoga.tests.fixture.object_mothers.pushes.web.WebPushesObjectMother.aWebPushSubscription
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.test_apis.NotificationsTestApi
import pro.qyoga.tests.fixture.test_apis.WebPushesTestApi
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


@DisplayName("Метод регистрации подписки на веб-пуш")
class RegisterSubscriptionApiTest : QYogaAppIntegrationBaseTest() {

    private val webPushesTestApi = getBean<WebPushesTestApi>()

    private val notificationsTestApi = getBean<NotificationsTestApi>()

    @Test
    fun `при передаче корректных данных должен сохранять подписку в БД для соответствующего терапевта и активировать ему уведомления о заполнении расписания`() {
        // Given
        val webPushesApi = TrainerAdvisorApis.WebPushes.therapistApi(theTherapist.authCookie)
        val webPushSubscription = aWebPushSubscription()

        // When
        webPushesApi.createSubscription(webPushSubscription)

        // Then
        val subscriptions = webPushesTestApi.getTherapistSubscriptions(THE_THERAPIST_REF)
        subscriptions.forAny { it shouldBe webPushSubscription }

        // And
        notificationsTestApi.isFillScheduleNotificationsEnabled(THE_THERAPIST_REF) shouldBe true
    }

    @Test
    fun `при повторной передаче корректных данных для существующего p256dh должен обновлять подписку в БД для соответствующего терапевта`() {
        // Given
        val webPushesApi = TrainerAdvisorApis.WebPushes.therapistApi(theTherapist.authCookie)
        val webPushSubscription = aWebPushSubscription()
        webPushesTestApi.createSubscription(THE_THERAPIST_REF, webPushSubscription)
        val secondWebPushSubscription = aWebPushSubscription(p256dh = webPushSubscription.keys.p256dh)

        // When
        webPushesApi.createSubscription(secondWebPushSubscription)

        // Then
        val subscriptions = webPushesTestApi.getTherapistSubscriptions(THE_THERAPIST_REF)
        subscriptions.forAny { it shouldBe secondWebPushSubscription }
        subscriptions.forAll { it shouldNotBe webPushSubscription }
    }

}
