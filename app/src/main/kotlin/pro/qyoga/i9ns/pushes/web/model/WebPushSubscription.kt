package pro.qyoga.i9ns.pushes.web.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class WebPushSubscription(
    @field:NotBlank
    @field:Size(max = 2048)
    val endpoint: String,
    val expirationTime: Long? = null,
    val keys: Keys,
) {

    data class Keys(
        @field:NotBlank
        @field:Size(max = 255)
        val p256dh: String,

        @field:NotBlank
        @field:Size(max = 255)
        val auth: String
    )

}
