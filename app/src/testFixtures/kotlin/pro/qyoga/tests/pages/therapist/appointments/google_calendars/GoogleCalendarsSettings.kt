package pro.qyoga.tests.pages.therapist.appointments.google_calendars

import io.kotest.matchers.should
import pro.qyoga.i9ns.calendars.google.views.GoogleAccountCalendarsSettingsView
import pro.qyoga.tests.assertions.*
import pro.qyoga.tests.platform.html.Link


object GoogleCalendarsSettings {

    private val connectButton =
        Link("connect-google-calendar", "/oauth2/authorization/google", "Добавить аккаунт")

    fun with(accounts: List<GoogleAccountCalendarsSettingsView>) =
        ComponentMatcher("#google-calendar-settings") {
            it[".google-account-item"].shouldMatch(accounts) { it, acc ->
                it(".account-email") should haveText(acc.email)
                if (acc.content.isError) {
                    it should haveComponent(".google-account-error-content")
                } else {
                    it[".calendar-list-item"].shouldMatch(acc.content.calendars) { it, cal ->
                        it(".calendar-name") should haveText(cal.title)
                        it(".should-be-shown-toggle") should haveCheckboxChecked(cal.shouldBeShown)
                    }
                }
            }

            it should haveComponent(connectButton)
        }

}

