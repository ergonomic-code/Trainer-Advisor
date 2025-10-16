package pro.qyoga.i9ns.pushes.web

import nl.martijndwars.webpush.Encoding
import nl.martijndwars.webpush.Notification
import nl.martijndwars.webpush.PushService
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import pro.qyoga.i9ns.pushes.web.model.WebPush
import pro.qyoga.i9ns.pushes.web.model.WebPushSubscription
import java.security.Security


@Component
class WebPushServiceClient(
    webPushesConfProps: WebPushesConfProps,
    @Value("\${trainer-advisor.admin.email}") adminEmail: String,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    private val pushService: PushService = PushService(
        webPushesConfProps.publicKey,
        webPushesConfProps.privateKey,
        "mailto:$adminEmail"
    )

    fun sendPush(subscription: WebPushSubscription, push: WebPush) {
        val notification = Notification(
            subscription.endpoint,
            subscription.keys.p256dh,
            subscription.keys.auth,
            push.toPayloadJson()
        )

        val resp = pushService.send(notification, Encoding.AES128GCM)
        if (resp.statusLine.statusCode != 201) {
            log.warn(
                "Push sending failed - statusCode={}, response={}",
                resp.statusLine.statusCode,
                resp.entity?.content?.bufferedReader()?.readText()
            )
        }
    }

}

private fun WebPush.toPayloadJson() =
    """
        {
            "title": "$title",
            "body": "$body",
            "data": {
                "deepLink": "$deepLink"
            }
        }
    """.trimIndent()
