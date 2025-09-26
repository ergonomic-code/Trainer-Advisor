package pro.qyoga.tests.fixture.oauth

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse
import org.springframework.web.util.UriComponentsBuilder
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.data.randomBase64String
import java.net.URI
import java.net.URLDecoder

object OAuthObjectMother {

    val googleAuthUri: URI = URI.create("https://accounts.google.com/o/oauth2/v2/auth")

    fun oAuth2AuthorizationRequest(redirectUri: URI): OAuth2AuthorizationRequest {
        val queryParams = UriComponentsBuilder.fromUri(redirectUri).build().queryParams

        return OAuth2AuthorizationRequest.authorizationCode()
            .authorizationUri(googleAuthUri.toString())
            .clientId(queryParams["client_id"]?.single()?.let { URLDecoder.decode(it, Charsets.UTF_8) })
            .scopes(queryParams["scope"]?.map { URLDecoder.decode(it, Charsets.UTF_8) }?.toSet()?.toSet())
            .state(queryParams["state"]?.single()?.let { URLDecoder.decode(it, Charsets.UTF_8) })
            .build()
    }

    fun aOAuth2AuthorizationResponse(state: String): OAuth2AuthorizationResponse =
        OAuth2AuthorizationResponse.success(faker.randomBase64String())
            .state(state)
            .redirectUri("/")
            .build()

}