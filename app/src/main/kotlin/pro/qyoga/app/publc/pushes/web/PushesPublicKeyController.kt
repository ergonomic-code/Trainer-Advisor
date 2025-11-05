package pro.qyoga.app.publc.pushes.web

import org.springframework.http.CacheControl
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pro.qyoga.i9ns.pushes.web.WebPushesConfProps
import java.time.Duration


@RestController
class PushesPublicKeyController(
    private val webPushesConfProps: WebPushesConfProps
) {

    @GetMapping(PUBLIC_KEY_PATH, produces = [MediaType.TEXT_PLAIN_VALUE])
    fun publicKey(): ResponseEntity<String> {
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(Duration.ofHours(1)).cachePublic())
            .body(webPushesConfProps.publicKey)
    }

    companion object {

        const val PUBLIC_KEY_PATH = "/pushes/web/public-key"

    }

}
