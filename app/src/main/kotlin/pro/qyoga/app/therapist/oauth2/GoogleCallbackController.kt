package pro.qyoga.app.therapist.oauth2

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.client.RestClient
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.Therapist
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.calendars.google.GoogleAccount
import pro.qyoga.i9ns.calendars.google.GoogleCalendarsService
import java.util.*

/**
 * Общая логика авторизации в Гугле:
 * 1. Пользователь в браузере нажимает кнопку "Подключить Google Calendar", которая ведёт на oauth2/authorization/google
 * 2. Этот эндпоинт обрабатывается Spring Security, которая складывает запрос на авторизацию в сессию и перенаправляет на https://accounts.google.com/o/oauth2/v2/auth
 * 3. Далее пользователь в Google выдаёт доступ приложению, после чего Google перенаправляет его на /therapist/oauth2/google/callback передавая параметры авторизации (code, state etc) в параметрах запроса
 * 4. Этот запрос обрабатывается Spring Security, которая сохраняет параметры в сессию и снова делает редирект на тот же эндпоинт, но уже без параметров
 * 5. Этот запрос снова сначала обрабатывается Spring Security, которая достаёт данные из сессии и идёт в гугл, чтобы обменять code на токены и собрать из них OAuth2AuthorizedClient
 * 6. После чего (уже без редиректа) запрос доходит в этот эндпоинт
 */
@Controller
class GoogleOAuthController(
    private val googleCalendarsService: GoogleCalendarsService
) {

    // Этот endpoint теперь будет работать с oauth2Client
    @GetMapping(PATH)
    fun handleOAuthCallback(
        @RegisteredOAuth2AuthorizedClient("google") authorizedClient: OAuth2AuthorizedClient,
        @AuthenticationPrincipal userDetails: QyogaUserDetails,
        @Value("\${spring.security.oauth2.client.provider.google.user-info-uri}") googleOicUserInfoUri: String
    ): String {
        val therapistId = TherapistRef.to<Therapist, UUID>(userDetails.id)

        val response = RestClient.create(googleOicUserInfoUri)
            .get()
            .headers { it.setBearerAuth(authorizedClient.accessToken.tokenValue) }
            .retrieve()
            .body(Map::class.java)
        val email = response
            ?.get("email") as String
        val picture = response["picture"] as? String?

        googleCalendarsService.addGoogleAccount(
            GoogleAccount(therapistId, email, authorizedClient.refreshToken!!.tokenValue)
        )

        // Греем кэш, чтобы улучшить UX пользователя при возврате на страницу расписания
        googleCalendarsService.findGoogleAccountCalendars(therapistId)

        return "redirect:/therapist/schedule?google-connected=true"
    }

    companion object {
        const val PATH = "/therapist/oauth2/google/callback"
    }

}
