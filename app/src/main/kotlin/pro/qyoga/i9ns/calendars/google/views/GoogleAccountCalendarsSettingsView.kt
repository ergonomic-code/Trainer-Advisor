package pro.qyoga.i9ns.calendars.google.views

import pro.qyoga.i9ns.calendars.google.model.GoogleAccount
import pro.qyoga.i9ns.calendars.google.model.GoogleCalendar
import pro.qyoga.i9ns.calendars.google.model.GoogleCalendarSettings
import java.util.*


private const val DEFAULT_CALENDAR_VISIBILITY = false

data class GoogleAccountCalendarsSettingsView(
    val id: UUID,
    val email: String,
    val content: GoogleCalendarsSettingsView
) {

    companion object {

        fun of(
            account: GoogleAccount,
            calendars: Result<List<GoogleCalendar>>,
            calendarSettings: Map<String, GoogleCalendarSettings>
        ): GoogleAccountCalendarsSettingsView = GoogleAccountCalendarsSettingsView(
            account.id,
            account.email,
            GoogleCalendarsSettingsView(calendars, calendarSettings)
        )
    }

}

sealed interface GoogleCalendarsSettingsView {

    val calendars: List<GoogleCalendarSettingsView>
    val isError: Boolean

    data class Calendars(
        override val calendars: List<GoogleCalendarSettingsView>,
    ) : GoogleCalendarsSettingsView {
        override val isError: Boolean = false
    }

    data object Error : GoogleCalendarsSettingsView {
        override val calendars: List<GoogleCalendarSettingsView> = emptyList()
        override val isError: Boolean = true
    }

    companion object {

        operator fun invoke(
            calendars: Result<List<GoogleCalendar>>,
            calendarSettings: Map<String, GoogleCalendarSettings>
        ): GoogleCalendarsSettingsView =
            if (calendars.isSuccess) {
                Calendars(calendars.getOrThrow().map {
                    GoogleCalendarSettingsView(
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

data class GoogleCalendarSettingsView(
    val id: String,
    val title: String,
    val shouldBeShown: Boolean
)
