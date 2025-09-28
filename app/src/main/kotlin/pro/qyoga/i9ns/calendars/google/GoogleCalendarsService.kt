package pro.qyoga.i9ns.calendars.google

import org.apache.tomcat.util.threads.VirtualThreadExecutor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import pro.azhidkov.platform.java.time.Interval
import pro.azhidkov.platform.java.time.zoneId
import pro.azhidkov.platform.kotlin.tryExecute
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.core.calendar.api.CalendarItem
import pro.qyoga.core.calendar.api.CalendarType
import pro.qyoga.core.calendar.api.CalendarsService
import pro.qyoga.core.calendar.api.SearchResult
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.calendars.google.client.GoogleCalendarsClient
import pro.qyoga.i9ns.calendars.google.model.GoogleAccount
import pro.qyoga.i9ns.calendars.google.model.GoogleAccountRef
import pro.qyoga.i9ns.calendars.google.model.GoogleCalendar
import pro.qyoga.i9ns.calendars.google.model.GoogleCalendarItemId
import pro.qyoga.i9ns.calendars.google.persistance.GoogleAccountsDao
import pro.qyoga.i9ns.calendars.google.persistance.GoogleCalendarSettingsPatch
import pro.qyoga.i9ns.calendars.google.persistance.GoogleCalendarsDao
import pro.qyoga.i9ns.calendars.google.views.GoogleAccountCalendarsSettingsView
import java.time.ZonedDateTime
import java.util.concurrent.CompletableFuture


@Service
class GoogleCalendarsService(
    private val googleAccountsDao: GoogleAccountsDao,
    private val googleCalendarsDao: GoogleCalendarsDao,
    private val googleCalendarsClient: GoogleCalendarsClient,
) : CalendarsService<GoogleCalendarItemId> {

    private val log = LoggerFactory.getLogger(javaClass)

    private val executor = VirtualThreadExecutor("google-calendar-events-fetcher")

    override val type: CalendarType = GoogleCalendar.Type

    fun addGoogleAccount(googleAccount: GoogleAccount) {
        googleAccountsDao.addGoogleAccount(googleAccount)
    }

    fun findGoogleAccountCalendars(
        therapist: TherapistRef
    ): List<GoogleAccountCalendarsSettingsView> {
        val accounts = googleAccountsDao.findGoogleAccounts(therapist)

        val accountCalendars = accounts.map { acc ->
            acc to googleCalendarsClient.getAccountCalendars(therapist, acc)
        }

        val calendarSettings = googleCalendarsDao.findCalendarsSettings(therapist)

        return accountCalendars.map { (account, calendars) ->
            GoogleAccountCalendarsSettingsView.of(account, calendars, calendarSettings)
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
            .mapNotNull {
                if (it.isFailure) {
                    log.warn("Failed to fetch events for account", it.exceptionOrNull())
                }
                it.getOrNull()
            }
            .flatMap { it }
            .map { it.toLocalizedCalendarItem(interval.zoneId) }

        return SearchResult(events, hasErrors = calendarEventsResults.any { it.isFailure })
    }

    override fun findById(
        therapistRef: TherapistRef,
        eventId: GoogleCalendarItemId
    ): CalendarItem<GoogleCalendarItemId, ZonedDateTime>? {
        // Выбирается список, потому что один терапевт может подключить несколько аккаунтов, между которыми
        // подключен один календарь. И для чтения события можно воспользоватья любым из этих аккаунтов
        val account = googleAccountsDao.findGoogleAccounts(therapistRef, eventId.calendarId).first()
        return googleCalendarsClient.findById(account, eventId)
    }

    override fun createItemId(itemId: Map<String, String?>): GoogleCalendarItemId {
        return GoogleCalendarItemId(itemId["cid"]!!, itemId["eid"]!!)
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
