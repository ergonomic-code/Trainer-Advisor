package pro.qyoga.core.calendar.google

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.UserCredentials
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties
import org.springframework.stereotype.Service
import pro.azhidkov.platform.java.time.Interval
import pro.qyoga.core.calendar.api.CalendarItem
import pro.qyoga.core.calendar.api.CalendarsService
import pro.qyoga.core.users.therapists.TherapistRef
import java.net.URI
import java.time.*


const val APPLICATION_NAME = "Trainer Advisor"
val gsonFactory: GsonFactory = GsonFactory.getDefaultInstance()
val httpTransport: NetHttpTransport = GoogleNetHttpTransport.newTrustedTransport()

data class GoogleCalendarView(
    val title: String,
    val shouldBeShown: Boolean
)

data class GoogleAccountCalendars(
    val email: String,
    val calendars: List<GoogleCalendarView>
)

@Service
class GoogleCalendarsService(
    private val googleOAuthProps: OAuth2ClientProperties,
    private val googleAccountsRepo: GoogleAccountsRepo,
    @Value("\${spring.security.oauth2.client.provider.google.token-uri}") private val tokenUri: URI,
    @Value("\${trainer-advisor.integrations.google-calendar.root-url}") private val googleCalendarRootUri: URI
) : CalendarsService {

    private val googleCalendarsRepo = GoogleCalendarsRepo()

    private val servicesCache = mutableMapOf<GoogleAccount, Calendar>()
        .withDefault { createCalendarService(it) }

    fun addGoogleAccount(googleAccount: GoogleAccount) {
        googleAccountsRepo.addGoogleAccount(googleAccount)
    }

    fun findGoogleAccountCalendars(
        therapist: TherapistRef
    ): List<GoogleAccountCalendars> {
        val accounts = googleAccountsRepo.findGoogleAccounts(therapist)
        return accounts.map {
            GoogleAccountCalendars(
                it.email,
                getAccountCalendars(therapist, it).map {
                    GoogleCalendarView(it.name, false)
                }
            )
        }
    }

    fun findCalendars(
        therapist: TherapistRef
    ): List<pro.qyoga.core.calendar.api.Calendar> {
        val accounts = googleAccountsRepo.findGoogleAccounts(therapist)
        if (accounts.isEmpty()) {
            return emptyList()
        }

        val account = accounts.single()

        return getAccountCalendars(therapist, account)
    }

    private fun getAccountCalendars(
        therapist: TherapistRef,
        account: GoogleAccount
    ): List<GoogleCalendar> {
        val service = servicesCache.getValue(account)

        return service.CalendarList().list()
            .execute().items.map {
                GoogleCalendar(therapist, it.id, it.summary)
            }
    }

    override fun findCalendarItemsInInterval(
        therapist: TherapistRef,
        interval: Interval<ZonedDateTime>
    ): Iterable<CalendarItem<*, LocalDateTime>> {
        val accounts = googleAccountsRepo.findGoogleAccounts(therapist)
        if (accounts.isEmpty()) {
            return emptyList()
        }

        val events = accounts.flatMap {
            val service = servicesCache.getValue(it)

            val events =
                service.events().list(it.email) // "primary" refers to the user's primary calendar
                    .setTimeMin(DateTime(interval.from.toInstant().toEpochMilli()))
                    .setTimeMax(DateTime(interval.to.toInstant().toEpochMilli()))
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute()
                    .items
                    .map {
                        println(it)
                        GoogleCalendarItem(
                            GoogleCalendarItemId(it.id),
                            it.summary,
                            it.description ?: "",
                            startDate(it),
                            duration(it),
                            it.location
                        )
                    }
            events
        }

        return events
    }

    private fun createCalendarService(account: GoogleAccount): Calendar {
        val credentials = UserCredentials.newBuilder()
            .setClientId(googleOAuthProps.registration["google"]!!.clientId)
            .setClientSecret(googleOAuthProps.registration["google"]!!.clientSecret)
            .setRefreshToken(account.refreshToken)
            .setRefreshToken(account.refreshToken)
            .setTokenServerUri(tokenUri)
            .build()

        val service = Calendar.Builder(httpTransport, gsonFactory, HttpCredentialsAdapter(credentials))
            .setApplicationName(APPLICATION_NAME)
            .setRootUrl(googleCalendarRootUri.toURL().toString())
            .build()
        return service
    }

    private fun startDate(event: Event): LocalDateTime =
        ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(event.start.dateTime?.value ?: event.start.date?.value ?: 0),
            ZoneId.of(event.start.timeZone)
        ).toLocalDateTime()

    private fun duration(event: Event): Duration =
        Duration.ofMillis(event.end.dateTime?.value ?: event.end.date?.value ?: 0) -
                Duration.ofMillis(event.start.dateTime?.value ?: event.start.date?.value ?: 0)

}