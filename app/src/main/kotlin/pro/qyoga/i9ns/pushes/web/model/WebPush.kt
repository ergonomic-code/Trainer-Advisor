package pro.qyoga.i9ns.pushes.web.model

import java.net.URI


data class WebPush(
    val title: String,
    val body: String,
    val deepLink: URI
)
