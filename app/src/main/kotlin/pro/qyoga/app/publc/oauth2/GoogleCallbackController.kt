package pro.qyoga.app.publc.oauth2

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.client.endpoint.RestClientAuthorizationCodeTokenResponseClient
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageController
import java.net.URI


@Controller
class GoogleCallbackController(
    private val authorizedClientRepo: OAuth2AuthorizedClientRepository
) {

    @GetMapping("/therapist/oauth2/google/save-tokens")
    fun handleCallback(
        @RequestParam code: String,
        @RequestParam state: String?,
        request: HttpServletRequest
    ): String {

        // 1. Вручную создаем OAuth2 клиента
        val clientRegistration = ClientRegistration.withRegistrationId("google")
            .clientId("clientId")
            .clientSecret("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")
            .scope("https://www.googleapis.com/auth/calendar.events.readonly")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("http://localhost:8080/therapist/oauth2/google/save-tokens")
            .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
            .tokenUri("https://oauth2.googleapis.com/token")
            .userInfoUri("https://openidconnect.googleapis.com/v1/userinfo")
            .userNameAttributeName("sub")
            .clientName("Google")
            .build()

        // 2. Обмениваем code на токены
        val tokenResponse = RestClientAuthorizationCodeTokenResponseClient()
            .getTokenResponse(
                OAuth2AuthorizationCodeGrantRequest(
                    clientRegistration,
                    OAuth2AuthorizationExchange(
                        OAuth2AuthorizationRequest
                            .authorizationCode()
                            .redirectUri("http://localhost:8080/therapist/oauth2/google/save-tokens")
                            .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                            .clientId("clientId")
                            .build(),
                        OAuth2AuthorizationResponse
                            .success(code)
                            .redirectUri("http://localhost:8080/therapist/oauth2/google/save-tokens")
                            .build()
                    )
                )
            )
        println(tokenResponse)
        println(tokenResponse.accessToken.tokenValue)
        println(tokenResponse.accessToken.expiresAt)
        println(tokenResponse.refreshToken?.tokenValue)

        // 3. Возвращаем токены (или сохраняем в БД)
        return "redirect:${SchedulePageController.PATH}"
    }

    @GetMapping("/oauth2/authorization/google")
    fun connectGoogle(): ResponseEntity<Void> {
        val authUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "response_type=code&" +
                "client_id=clientId&" +
                "scope=https://www.googleapis.com/auth/calendar.readonly&" +
                "redirect_uri=http://localhost:8080/therapist/oauth2/google/save-tokens&" +
                "access_type=offline&" +
                "prompt=consent"

        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(authUrl))
            .build()
    }

}