package pro.qyoga.i9ns.calendars.google

import jakarta.annotation.PreDestroy
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
import pro.qyoga.i9ns.calendars.google.model.*
import pro.qyoga.i9ns.calendars.google.persistance.GoogleAccountsDao
import pro.qyoga.i9ns.calendars.google.persistance.GoogleCalendarSettingsPatch
import pro.qyoga.i9ns.calendars.google.persistance.GoogleCalendarsDao
import pro.qyoga.i9ns.calendars.google.views.GoogleAccountCalendarsSettingsView
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.supplyAsync
import java.util.concurrent.Executor
import java.util.concurrent.Executors


private typealias GoogleCalendarItemsFetchResult = Result<List<GoogleCalendarItem<ZonedDateTime>>>

private val log = LoggerFactory.getLogger(GoogleCalendarsService::class.java)

@Service
class GoogleCalendarsService(
    private val googleAccountsDao: GoogleAccountsDao,
    private val googleCalendarsDao: GoogleCalendarsDao,
    private val googleCalendarsClient: GoogleCalendarsClient,
) : CalendarsService<GoogleCalendarItemId> {

    private val executor = Executors.newVirtualThreadPerTaskExecutor()

    override val type: CalendarType = GoogleCalendar.Type

    @PreDestroy
    fun tearDown() {
        executor.shutdown()
    }

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
            .associateBy { it.calendarId }

        return accountCalendars.map { (account, calendars) ->
            GoogleAccountCalendarsSettingsView.of(account, calendars, calendarSettings)
        }
    }

    override fun findCalendarItemsInInterval(
        therapist: TherapistRef,
        interval: Interval<ZonedDateTime>
    ): SearchResult<GoogleCalendarItemId> {
        val enabledGoogleCalendars = googleCalendarsDao.findCalendarsSettings(
            therapist = therapist,
            shouldBeShown = true
        )
        if (enabledGoogleCalendars.isEmpty()) {
            return SearchResult(emptyList())
        }

        val accountCalendars = enabledGoogleCalendars.groupBy { it.googleAccountRef.id!! }
        val accountIds = enabledGoogleCalendars.map { it.googleAccountRef }
            .distinct()
        val accounts = googleAccountsDao.findGoogleAccounts(accountIds)

        val calendarEventsFetchResults: List<GoogleCalendarItemsFetchResult> =
            createFetchTasks(accounts, accountCalendars, interval) { account, interval, calendarId ->
                tryExecute {
                    googleCalendarsClient.getEvents(account, interval, calendarId)
                }.onFailure {
                    log.warn("Failed to fetch events for account", it)
                }
            }
                .submitAll(executor)
                .awaitAll()

        val items = calendarEventsFetchResults
            .mapNotNull { it.getOrNull() }
            .flatten()
            .map { it.toLocalizedCalendarItem(interval.zoneId) }

        return SearchResult(
            items = items,
            hasErrors = calendarEventsFetchResults.any { it.isFailure }
        )
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

private fun createFetchTasks(
    accounts: List<GoogleAccount>,
    settingsByAccount: Map<UUID, List<GoogleCalendarSettings>>,
    interval: Interval<ZonedDateTime>,
    fetch: (GoogleAccount, Interval<ZonedDateTime>, GoogleCalendarId) -> GoogleCalendarItemsFetchResult
): List<() -> GoogleCalendarItemsFetchResult> =
    accounts.flatMap { account ->
        val settings = settingsByAccount[account.ref().id!!]
            ?: return@flatMap emptyList()

        settings
            .map { settings -> { fetch(account, interval, settings.calendarId) } }
    }

private fun List<() -> GoogleCalendarItemsFetchResult>.submitAll(executor: Executor): List<CompletableFuture<GoogleCalendarItemsFetchResult>> =
    this.map { task ->
        supplyAsync(task, executor)
    }

private fun List<CompletableFuture<GoogleCalendarItemsFetchResult>>.awaitAll(): List<GoogleCalendarItemsFetchResult> =
    this.map(CompletableFuture<GoogleCalendarItemsFetchResult>::get)
