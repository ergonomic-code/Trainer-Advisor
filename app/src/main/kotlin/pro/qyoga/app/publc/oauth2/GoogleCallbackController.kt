package pro.qyoga.app.publc.oauth2

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.Therapist
import pro.qyoga.core.users.therapists.TherapistRef
import java.util.*

@Controller
class GoogleOAuthController(
) {

    companion object {

        var token = ""
    }

    // Этот endpoint теперь будет работать с oauth2Client
    @GetMapping("/therapist/oauth2/google/callback")
    fun handleOAuthCallback(
        @RegisteredOAuth2AuthorizedClient("google") authorizedClient: OAuth2AuthorizedClient,
        @AuthenticationPrincipal userDetails: QyogaUserDetails
    ): String {
        val therapistId = TherapistRef.to<Therapist, UUID>(userDetails.id)

        token = authorizedClient.accessToken.tokenValue
        // Spring автоматически получил токены!
        println("Access Token: ${authorizedClient.accessToken.tokenValue}")
        println("Refresh Token: ${authorizedClient.refreshToken?.tokenValue}")
        println("Expires At: ${authorizedClient.accessToken.expiresAt}")

        // Сохраняем токены
        // googleOAuthService.saveAuthorizedClient(therapistId, authorizedClient)

        return "redirect:/therapist/schedule?google_connected=true"
    }

}