package pro.qyoga.tests.assertions

import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.uri.shouldHaveHost
import io.kotest.matchers.uri.shouldHavePath
import io.kotest.matchers.uri.shouldHaveScheme
import org.springframework.web.util.UriComponentsBuilder
import pro.qyoga.app.therapist.oauth2.GoogleOAuthController
import pro.qyoga.tests.fixture.oauth.OAuthObjectMother
import java.net.URI

// https://accounts.google.com/o/oauth2/v2/auth?access_type=offline&prompt=consent&response_type=code&client_id=000000000000-aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.apps.googleusercontent.com&scope=https://www.googleapis.com/auth/calendar.readonly&state=hpPCah40YNgllwaMf4MPQdiGGedlmOJHq33zGtuvlAU%3D&redirect_uri=http://localhost:8080/therapist/oauth2/google/callback
fun URI.shouldBeRedirectToGoogleOAuth(clientId: String) {
    this shouldHaveScheme OAuthObjectMother.googleAuthUri.scheme
    this shouldHaveHost OAuthObjectMother.googleAuthUri.host
    this shouldHavePath OAuthObjectMother.googleAuthUri.path

    val queryParams = UriComponentsBuilder.fromUri(this).build().queryParams
    queryParams.shouldContainValue("access_type", "offline")
    queryParams.shouldContainValue("prompt", "consent")
    queryParams.shouldContainValue("response_type", "code")
    queryParams.shouldContainValue("client_id", clientId)
    queryParams.shouldContainValue("scope", "openid%20email%20https://www.googleapis.com/auth/calendar.readonly")
    queryParams.shouldContainKey("state")
    queryParams["redirect_uri"]!!.single() shouldEndWith GoogleOAuthController.PATH
}