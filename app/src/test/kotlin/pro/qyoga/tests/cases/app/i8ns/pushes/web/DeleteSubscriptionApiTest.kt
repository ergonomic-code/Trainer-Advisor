package pro.qyoga.tests.cases.app.i8ns.pushes.web

import io.kotest.inspectors.forNone
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.qyoga.tests.clients.api.TrainerAdvisorApis
import pro.qyoga.tests.fixture.object_mothers.pushes.web.WebPushesObjectMother.aWebPushSubscription
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.test_apis.WebPushesTestApi
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


@DisplayName("Метод удаления подписки веб-пушей")
class DeleteSubscriptionApiTest : QYogaAppIntegrationBaseTest() {

    private val webPushesTestApi = getBean<WebPushesTestApi>()

    @Test
    fun `при передаче корректного p256dh должен удалять подписку из БД`() {
        // Given
        val webPushesApi = TrainerAdvisorApis.WebPushes.therapistApi(theTherapist.authCookie)
        val webPushSubscription = aWebPushSubscription()
        webPushesTestApi.createSubscription(THE_THERAPIST_REF, webPushSubscription)

        // When
        webPushesApi.deleteSubscription(webPushSubscription.keys.p256dh)

        // Then
        webPushesTestApi.getTherapistSubscriptions(THE_THERAPIST_REF).forNone { it shouldBe webPushSubscription }
    }

}
