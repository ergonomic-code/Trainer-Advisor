package pro.qyoga.core.calendar.google

import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.UserCredentials
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import pro.azhidkov.platform.java.time.Interval
import pro.qyoga.core.users.therapists.TherapistRef
import java.net.URI
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime


@Component
class GoogleCalendarsClient(
    private val googleOAuthProps: OAuth2ClientProperties,
    @Value("\${spring.security.oauth2.client.provider.google.token-uri}") private val tokenUri: URI,
    @Value("\${trainer-advisor.integrations.google-calendar.root-url}") private val googleCalendarRootUri: URI
) {

    private val servicesCache = mutableMapOf<GoogleAccount, Calendar>()
        .withDefault { createCalendarService(it) }

    @Cacheable(
        cacheNames = [GoogleCalendarConf.CacheNames.CALENDAR_EVENTS],
        key = "#calendarSettings.calendarId + ':' + #interval.from.toInstant().toEpochMilli() + ':' + #interval.to.toInstant().toEpochMilli()"
    )
    fun getEvents(
        account: GoogleAccount,
        calendarSettings: GoogleCalendarSettings,
        interval: Interval<ZonedDateTime>
    ): List<GoogleCalendarItem<ZonedDateTime>> {
        val service = servicesCache.getValue(account)
        val events =
            service.events().list(calendarSettings.calendarId)
                .setTimeMin(DateTime(interval.from.toInstant().toEpochMilli()))
                .setTimeMax(DateTime(interval.to.toInstant().toEpochMilli()))
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute()
                .items
                .map {
                    GoogleCalendarItem(
                        GoogleCalendarItemId(it.id),
                        it.summary,
                        it.description ?: "",
                        startDate(it),
                        duration(it),
                        it.location
                    )
                }
        return events
    }

    private fun startDate(event: Event): ZonedDateTime =
        ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(event.start.dateTime?.value ?: event.start.date?.value ?: 0),
            ZoneId.of(event.start.timeZone)
        )

    private fun duration(event: Event): Duration =
        Duration.ofMillis(event.end.dateTime?.value ?: event.end.date?.value ?: 0) -
                Duration.ofMillis(event.start.dateTime?.value ?: event.start.date?.value ?: 0)

    @Cacheable(
        cacheNames = [GoogleCalendarConf.CacheNames.GOOGLE_ACCOUNT_CALENDARS],
        key = "#therapist.id + ':' + #account.id"
    )
    fun getAccountCalendars(
        therapist: TherapistRef,
        account: GoogleAccount
    ): List<GoogleCalendar> {
        val service = servicesCache.getValue(account)

        return service.CalendarList().list()
            .execute().items.map {
                GoogleCalendar(therapist, it.id, it.summary)
            }
    }

    private fun createCalendarService(account: GoogleAccount): Calendar {
        val credentials = UserCredentials.newBuilder()
            .setClientId(googleOAuthProps.registration["google"]!!.clientId)
            .setClientSecret(googleOAuthProps.registration["google"]!!.clientSecret)
            .setRefreshToken(account.refreshToken)
            .setTokenServerUri(tokenUri)
            .build()

        val service = Calendar.Builder(httpTransport, gsonFactory, HttpCredentialsAdapter(credentials))
            .setApplicationName(APPLICATION_NAME)
            .setRootUrl(googleCalendarRootUri.toURL().toString())
            .build()
        return service
    }

}