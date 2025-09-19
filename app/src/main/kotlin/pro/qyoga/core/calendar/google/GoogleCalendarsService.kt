package pro.qyoga.core.calendar.google

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.apache.tomcat.util.threads.VirtualThreadExecutor
import org.springframework.stereotype.Service
import pro.azhidkov.platform.java.time.Interval
import pro.azhidkov.platform.java.time.zoneId
import pro.azhidkov.platform.kotlin.tryExecute
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.core.calendar.api.CalendarItem
import pro.qyoga.core.calendar.api.CalendarsService
import pro.qyoga.core.calendar.api.SearchResult
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.CompletableFuture


const val APPLICATION_NAME = "Trainer Advisor"
val gsonFactory: GsonFactory = GsonFactory.getDefaultInstance()
val httpTransport: NetHttpTransport = GoogleNetHttpTransport.newTrustedTransport()

data class GoogleCalendarView(
    val id: String,
    val title: String,
    val shouldBeShown: Boolean
)

private const val DEFAULT_CALENDAR_VISIBILITY = false

sealed interface GoogleAccountContentView {
    data class Calendars(val calendars: List<GoogleCalendarView>) : GoogleAccountContentView
    data object Error : GoogleAccountContentView

    companion object {
        operator fun invoke(
            calendars: Result<List<GoogleCalendar>>,
            calendarSettings: Map<String, GoogleCalendarSettings>
        ): GoogleAccountContentView =
            if (calendars.isSuccess) {
                Calendars(calendars.getOrThrow().map {
                    GoogleCalendarView(
                        it.externalId,
                        it.name,
                        calendarSettings[it.externalId]?.shouldBeShown ?: DEFAULT_CALENDAR_VISIBILITY
                    )
                })
            } else {
                Error
            }
    }
}

data class GoogleAccountCalendarsView(
    val id: UUID,
    val email: String,
    val content: GoogleAccountContentView
) {

    companion object {

        fun of(
            account: GoogleAccount,
            calendars: Result<List<GoogleCalendar>>,
            calendarSettings: Map<String, GoogleCalendarSettings>
        ): GoogleAccountCalendarsView = GoogleAccountCalendarsView(
            account.id,
            account.email,
            GoogleAccountContentView(calendars, calendarSettings)
        )
    }

}

@Service
class GoogleCalendarsService(
    private val googleAccountsDao: GoogleAccountsDao,
    private val googleCalendarsDao: GoogleCalendarsDao,
    private val googleCalendarsClient: GoogleCalendarsClient,
) : CalendarsService<GoogleCalendarItemId> {

    private val executor = VirtualThreadExecutor("google-calendar-events-fetcher")

    fun addGoogleAccount(googleAccount: GoogleAccount) {
        googleAccountsDao.addGoogleAccount(googleAccount)
    }

    fun findGoogleAccountCalendars(
        therapist: TherapistRef
    ): List<GoogleAccountCalendarsView> {
        val accounts = googleAccountsDao.findGoogleAccounts(therapist)
        val accountCalendars = accounts.map {
            googleCalendarsClient.getAccountCalendars(therapist, it)
        }
        val calendarSettings = googleCalendarsDao.findCalendarsSettings(therapist)
        return accounts.zip(accountCalendars).map { (account, calendars) ->
            GoogleAccountCalendarsView.of(account, calendars, calendarSettings)
        }
    }

    override fun findCalendarItemsInInterval(
        therapist: TherapistRef,
        interval: Interval<ZonedDateTime>
    ): SearchResult<GoogleCalendarItemId> {
        val googleCalendarSettings = googleCalendarsDao.findCalendarsSettings(therapist)
        if (googleCalendarSettings.isEmpty()) {
            return SearchResult(emptyList())
        }
        val accountCalendars = googleCalendarSettings.values.groupBy { it.googleAccountRef.id }
        val accountIds = googleCalendarSettings.values.map { it.googleAccountRef }
            .distinct()
        val accounts = googleAccountsDao.findGoogleAccounts(accountIds)

        val fetchTasks = accounts
            .flatMap { account ->

                val settings = accountCalendars[account.ref().id]
                    ?: return@flatMap emptyList()

                settings
                    .filter { it.shouldBeShown }
                    .map { calendarSettings ->
                        CompletableFuture.supplyAsync(
                            {
                                googleCalendarsClient.getEvents(account, calendarSettings, interval)
                            }, executor
                        )
                    }
            }
        val calendarEventsResults = fetchTasks.map {
            tryExecute { it.get() }
        }

        val events = calendarEventsResults
            .mapNotNull { it.getOrNull() }
            .flatMap { it }
            .map { it.toLocalizedCalendarItem(interval.zoneId) }

        return SearchResult(events, hasErrors = calendarEventsResults.any { it.isFailure })
    }

    override fun findById(
        therapistRef: TherapistRef,
        eventId: GoogleCalendarItemId
    ): CalendarItem<GoogleCalendarItemId, ZonedDateTime>? {
        val account = googleAccountsDao.findGoogleAccount(therapistRef, eventId.calendarId).first()
        return googleCalendarsClient.findById(account, eventId)
    }

    fun updateCalendarSettings(
        therapist: TherapistRef,
        googleAccount: GoogleAccountRef,
        calendarId: String,
        settingsPatch: GoogleCalendarSettingsPatch
    ) {
        googleCalendarsDao.patchCalendarSettings(therapist, googleAccount, calendarId, settingsPatch)
    }

}
