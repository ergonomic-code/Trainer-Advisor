package pro.qyoga.tests.fixture.object_mothers.pushes.web

import pro.qyoga.i9ns.pushes.web.model.WebPushSubscription
import pro.qyoga.tests.fixture.data.faker
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.interfaces.ECPublicKey
import java.security.spec.ECGenParameterSpec
import java.util.*


object WebPushesObjectMother {

    fun aWebPushSubscription(
        p256dh: String = generateP256dh(),
        endpoint: String = anEndpoint()
    ) =
        WebPushSubscription(
            endpoint = endpoint,
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

    fun aFcmRegistrationToken(): String {
        val prefix = base64Url(8)
        val main = "APA91b" + base64Url(110)
        return "$prefix:$main"
    }

    private fun generateP256dh(): String {
        val keyPairGenerator = KeyPairGenerator.getInstance("EC")
        keyPairGenerator.initialize(ECGenParameterSpec("secp256r1"))
        val publicKey = keyPairGenerator.generateKeyPair().public as ECPublicKey
        val affinePoint = publicKey.w

        val x = affinePoint.affineX.toEccCoordinate()
        val y = affinePoint.affineY.toEccCoordinate()

        val publicKeyBytes = ByteArray(1 + x.size + y.size)
        publicKeyBytes[0] = 0x04
        System.arraycopy(x, 0, publicKeyBytes, 1, x.size)
        System.arraycopy(y, 0, publicKeyBytes, 1 + x.size, y.size)

        return Base64.getUrlEncoder().withoutPadding().encodeToString(publicKeyBytes)
    }

    private fun BigInteger.toEccCoordinate(): ByteArray {
        val raw = toByteArray()
        if (raw.size == 32) {
            return raw
        }

        val withoutSignByte = if (raw.size > 32 && raw[0] == 0.toByte()) raw.copyOfRange(1, raw.size) else raw
        if (withoutSignByte.size == 32) {
            return withoutSignByte
        }

        if (withoutSignByte.size > 32) {
            return withoutSignByte.copyOfRange(withoutSignByte.size - 32, withoutSignByte.size)
        }

        val padded = ByteArray(32)
        System.arraycopy(withoutSignByte, 0, padded, 32 - withoutSignByte.size, withoutSignByte.size)
        return padded
    }

    private fun base64Url(numBytes: Int): String {
        val bytes = faker.random().nextRandomBytes(numBytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

}
