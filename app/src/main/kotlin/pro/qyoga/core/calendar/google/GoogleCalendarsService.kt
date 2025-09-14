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
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
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
    val id: String,
    val title: String,
    val shouldBeShown: Boolean
)

data class GoogleAccountCalendarsView(
    val email: String,
    val calendars: List<GoogleCalendarView>
)

@Service
class GoogleCalendarsService(
    private val googleOAuthProps: OAuth2ClientProperties,
    private val googleAccountsDao: GoogleAccountsDao,
    private val googleCalendarsDao: GoogleCalendarsDao,
    @Value("\${spring.security.oauth2.client.provider.google.token-uri}") private val tokenUri: URI,
    @Value("\${trainer-advisor.integrations.google-calendar.root-url}") private val googleCalendarRootUri: URI
) : CalendarsService {

    private val servicesCache = mutableMapOf<GoogleAccount, Calendar>()
        .withDefault { createCalendarService(it) }

    @CacheEvict(
        cacheNames = [GoogleCalendarConf.CacheNames.GOOGLE_ACCOUNT_CALENDARS],
        key = "#googleAccount.ownerRef.id"
    )
    fun addGoogleAccount(googleAccount: GoogleAccount) {
        googleAccountsDao.addGoogleAccount(googleAccount)
    }

    fun findGoogleAccountCalendars(
        therapist: TherapistRef
    ): List<GoogleAccountCalendarsView> {
        val accounts = googleAccountsDao.findGoogleAccounts(therapist)
        val accountCalendars = accounts.map {
            getAccountCalendars(therapist, it)
        }
        val calendarSettings = googleCalendarsDao.findCalendarsSettings(therapist)
        return accounts.zip(accountCalendars).map { (account, calendar) ->
            GoogleAccountCalendarsView(
                account.email,
                calendar.map {
                    GoogleCalendarView(it.externalId, it.name, calendarSettings[it.externalId]?.shouldBeShown ?: false)
                }
            )
        }
    }

    fun findCalendars(
        therapist: TherapistRef
    ): List<pro.qyoga.core.calendar.api.Calendar> {
        val accounts = googleAccountsDao.findGoogleAccounts(therapist)
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

    @Cacheable(
        cacheNames = [GoogleCalendarConf.CacheNames.CALENDAR_EVENTS],
        key = "#therapist.id + ':' + #interval.from.toInstant().toEpochMilli() + ':' + #interval.to.toInstant().toEpochMilli()"
    )
    override fun findCalendarItemsInInterval(
        therapist: TherapistRef,
        interval: Interval<ZonedDateTime>
    ): Iterable<CalendarItem<*, LocalDateTime>> {
        val accounts = googleAccountsDao.findGoogleAccounts(therapist)
        if (accounts.isEmpty()) {
            return emptyList()
        }

        val events = accounts.flatMap {
            val service = servicesCache.getValue(it)

            val events =
                service.events().list(it.email)
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
            events
        }

        return events
    }

    @CacheEvict(
        cacheNames = [GoogleCalendarConf.CacheNames.GOOGLE_ACCOUNT_CALENDARS],
        key = "#therapist.id"
    )
    fun updateCalendarSettings(
        therapist: TherapistRef,
        calendarId: String,
        settingsPatch: GoogleCalendarSettingsPatch
    ) {
        googleCalendarsDao.patchCalendarSettings(therapist, calendarId, settingsPatch)
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

    private fun startDate(event: Event): LocalDateTime =
        ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(event.start.dateTime?.value ?: event.start.date?.value ?: 0),
            ZoneId.of(event.start.timeZone)
        ).toLocalDateTime()

    private fun duration(event: Event): Duration =
        Duration.ofMillis(event.end.dateTime?.value ?: event.end.date?.value ?: 0) -
                Duration.ofMillis(event.start.dateTime?.value ?: event.start.date?.value ?: 0)

}