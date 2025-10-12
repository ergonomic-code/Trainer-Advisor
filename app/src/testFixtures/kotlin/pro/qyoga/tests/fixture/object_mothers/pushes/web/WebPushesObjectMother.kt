package pro.qyoga.tests.fixture.object_mothers.pushes.web

import pro.qyoga.i9ns.pushes.web.WebPushSubscription
import pro.qyoga.tests.fixture.data.faker
import java.util.*


object WebPushesObjectMother {

    fun aWebPushSubscription(p256dh: String = base64Url(43)) =
        WebPushSubscription(
            endpoint = anEndpoint(),
            null,
            keys = WebPushSubscription.Keys(
                p256dh = p256dh,
                auth = base64Url(16)
            )
        )

    private fun anEndpoint(): String {
        val baseUrl = "https://jmt17.google.com/fcm/send"
        val token = aFcmRegistrationToken()
        return "$baseUrl/$token"
    }

    private fun aFcmRegistrationToken(): String {
        val prefix = base64Url(8)
        val main = "APA91b" + base64Url(110)
        return "$prefix:$main"
    }

    private fun base64Url(numBytes: Int): String {
        val bytes = faker.random().nextRandomBytes(numBytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

}
