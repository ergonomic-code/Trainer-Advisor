package pro.qyoga.i9ns.calendars.google.client

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.UserCredentials
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import pro.azhidkov.platform.java.time.Interval
import pro.azhidkov.platform.kotlin.tryExecute
import pro.azhidkov.platform.kotlin.tryRecover
import pro.qyoga.core.calendar.api.CalendarItem
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.calendars.google.GoogleCalendarConf
import pro.qyoga.i9ns.calendars.google.model.*
import java.net.URI
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success


private const val APPLICATION_NAME = "Trainer Advisor"
private val gsonFactory: GsonFactory = GsonFactory.getDefaultInstance()
private val httpTransport: NetHttpTransport = GoogleNetHttpTransport.newTrustedTransport()

@Component
class GoogleCalendarsClient(
    private val googleOAuthProps: OAuth2ClientProperties,
    @Value("\${spring.security.oauth2.client.provider.google.token-uri}") private val tokenUri: URI,
    @Value("\${trainer-advisor.integrations.google-calendar.root-url}") private val googleCalendarRootUri: URI
) {

    private val log = LoggerFactory.getLogger(javaClass)

    private val calendarServicesCache = mutableMapOf<GoogleAccount, Calendar>()
        .withDefault { createCalendarService(it) }

    @Cacheable(
        cacheNames = [GoogleCalendarConf.CacheNames.CALENDAR_EVENTS],
        key = "#calendarId + ':' + #interval.from.toInstant().toEpochMilli() + ':' + #interval.to.toInstant().toEpochMilli()"
    )
    fun getEvents(
        account: GoogleAccount,
        interval: Interval<ZonedDateTime>,
        calendarId: String
    ): List<GoogleCalendarItem<ZonedDateTime>> {
        log.info("Fetching events in {} for calendar {} using {}", interval, calendarId, account)

        val service = calendarServicesCache.getValue(account)
        val events =
            service.events().list(calendarId)
                .setTimeMin(DateTime(interval.from.toInstant().toEpochMilli()))
                .setTimeMax(DateTime(interval.to.toInstant().toEpochMilli()))
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute()
                .items
                .map { mapToCalendarItem(calendarId, it) }
        return events
    }

    @Cacheable(
        cacheNames = [GoogleCalendarConf.CacheNames.GOOGLE_ACCOUNT_CALENDARS],
        key = "#therapist.id + ':' + #account.id"
    )
    fun getAccountCalendars(
        therapist: TherapistRef,
        account: GoogleAccount
    ): Result<List<GoogleCalendar>> {
        log.info("Fetching calendars for therapist {} using {}", therapist, account)
        val service = calendarServicesCache.getValue(account)

        val getCalendarsListRequest = service.CalendarList().list()

        val calendarListDto = tryExecute { getCalendarsListRequest.execute() }
            .getOrElse {
                log.warn("Failed to fetch calendars for therapist {} using {}", therapist, account, it)
                return failure(it)
            }

        val calendarsList = calendarListDto.items.map {
            GoogleCalendar(therapist, it.id, it.summary)
        }

        return success(calendarsList)
    }

    @Cacheable(
        cacheNames = [GoogleCalendarConf.CacheNames.CALENDAR_EVENTS],
        key = "#eventId"
    )
    fun findById(
        account: GoogleAccount,
        eventId: GoogleCalendarItemId
    ): CalendarItem<GoogleCalendarItemId, ZonedDateTime>? {
        val service = calendarServicesCache.getValue(account)

        val getEventRequest = service.events().get(eventId.calendarId, eventId.itemId)

        val event = tryExecute { getEventRequest.execute() }
            .tryRecover<GoogleJsonResponseException, _, _> {
                if (it.statusCode == HttpStatus.NOT_FOUND.value()) {
                    success(null)
                } else {
                    failure(it)
                }
            }
            .getOrThrow()

        return event?.let { mapToCalendarItem(eventId.calendarId, it) }
    }

    private fun createCalendarService(account: GoogleAccount): Calendar {
        val credentials = UserCredentials.newBuilder()
            .setClientId(googleOAuthProps.registration["google"]!!.clientId)
            .setClientSecret(googleOAuthProps.registration["google"]!!.clientSecret)
            .setRefreshToken(account.refreshToken.show())
            .setTokenServerUri(tokenUri)
            .build()

        val service = Calendar.Builder(httpTransport, gsonFactory, HttpCredentialsAdapter(credentials))
            .setApplicationName(APPLICATION_NAME)
            .setRootUrl(googleCalendarRootUri.toURL().toString())
            .build()
        return service
    }

}

private fun mapToCalendarItem(calendarId: String, event: Event): GoogleCalendarItem<ZonedDateTime> = GoogleCalendarItem(
    GoogleCalendarItemId(calendarId, event.id),
    event.summary,
    event.description ?: "",
    startDate(event),
    duration(event),
    event.location
)

private fun startDate(event: Event): ZonedDateTime =
    ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(event.start.dateTime?.value ?: event.start.date?.value ?: 0),
        ZoneId.of(event.start.timeZone)
    )

private fun duration(event: Event): Duration =
    Duration.ofMillis(event.end.dateTime?.value ?: event.end.date?.value ?: 0) -
            Duration.ofMillis(event.start.dateTime?.value ?: event.start.date?.value ?: 0)
