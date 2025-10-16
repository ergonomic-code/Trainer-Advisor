package pro.qyoga.i9ns.pushes.web

import nl.martijndwars.webpush.*
import org.bouncycastle.jce.interfaces.ECPublicKey
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import pro.qyoga.i9ns.pushes.web.model.WebPush
import pro.qyoga.i9ns.pushes.web.model.WebPushSubscription
import java.security.Security
import java.time.Duration
import java.util.*


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
        val notification = createNotification(subscription, push)

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

private fun createNotification(
    subscription: WebPushSubscription,
    push: WebPush
): Notification = Notification(
    /* endpoint = */ subscription.endpoint,
    /* userPublicKey = */ Utils.loadPublicKey(subscription.keys.p256dh) as ECPublicKey,
    /* userAuth = */ Base64.getUrlDecoder().decode(subscription.keys.auth),
    /* payload = */ push.toPayloadJson().toByteArray(),
    /* ttl = */ Duration.ofDays(7).toSeconds().toInt(),
    /* urgency = */ Urgency.NORMAL,
    /* topic = */ push.topic
)

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
