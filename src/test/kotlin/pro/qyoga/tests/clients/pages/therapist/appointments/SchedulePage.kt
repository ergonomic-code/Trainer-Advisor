package pro.qyoga.tests.clients.pages.therapist.appointments

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.core.appointments.core.model.Appointment
import pro.qyoga.l10n.russianTimeFormat
import pro.qyoga.tests.assertions.alwaysSuccess
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.assertions.haveElements
import pro.qyoga.tests.assertions.haveTitle
import pro.qyoga.tests.infra.html.Component
import pro.qyoga.tests.infra.html.HtmlPage
import pro.qyoga.tests.infra.html.Link

private const val BASE_PATH = "/therapist/schedule"

abstract class SchedulePage(val content: Component, override val path: String = BASE_PATH) : HtmlPage {

    final override val title = "Расписание"

    private val addAppointmentLink =
        Link("addAppointmentLink", CreateAppointmentPage, "Запланировать приём")

    override val matcher = Matcher.all(
        haveTitle(title),
        haveComponent(addAppointmentLink),
        haveComponent(SchedulePageTabs),
        haveComponent(content)
    )

    companion object {
        const val PATH = BASE_PATH
    }

}

object EmptyFutureSchedulePageTab : Component {

    private val addFirstAppointmentLink = Link("addFirstAppointmentLink", CreateAppointmentPage, "Запланировать первый")

    private val noAppointmentsMessageSelector =
        "${selector()}:contains(У вас не запланировано ни одного приёма. ${addFirstAppointmentLink.text})"

    override fun selector() = "#noAppointmentsMessage"

    override fun matcher() = Matcher.all(
        haveComponent(addFirstAppointmentLink),
        haveElements(noAppointmentsMessageSelector, 1)
    )

}

object EmptyFutureSchedulePage : SchedulePage(EmptyFutureSchedulePageTab)

object FutureSchedulePageTab : Component {

    const val TODAY_APPOINTMENTS_ID = "todayAppointments"

    const val NEXT_WEEK_APPOINTMENTS_ID = "nextWeekAppointments"

    const val LATER_APPOINTMENTS_ID = "laterAppointments"

    override fun selector() = "#futureAppointments"

    override fun matcher() = Matcher.all(
        haveElements("#${TODAY_APPOINTMENTS_ID}", 1),
        haveElements("#${NEXT_WEEK_APPOINTMENTS_ID}", 1),
        haveElements("#${LATER_APPOINTMENTS_ID}", 1),
    )

}

object PastSchedulePageTab : Component {

    override fun selector() = "#pastAppointments"

    override fun matcher(): Matcher<Element> {
        return alwaysSuccess()
    }

}

object FutureSchedulePage : SchedulePage(FutureSchedulePageTab) {

    fun today(document: Document): Element = document.getElementById(FutureSchedulePageTab.TODAY_APPOINTMENTS_ID)
        ?: error("Cannot find today appointments by ${FutureSchedulePageTab.TODAY_APPOINTMENTS_ID}")

    fun nextWeek(document: Document): Element = document.getElementById(FutureSchedulePageTab.NEXT_WEEK_APPOINTMENTS_ID)
        ?: error("Cannot find today appointments by ${FutureSchedulePageTab.NEXT_WEEK_APPOINTMENTS_ID}")

    fun later(document: Document): Element = document.getElementById(FutureSchedulePageTab.LATER_APPOINTMENTS_ID)
        ?: error("Cannot find today appointments by ${FutureSchedulePageTab.LATER_APPOINTMENTS_ID}")

    fun rowsFor(appointments: List<Appointment>): Matcher<Element> {
        val matchers = appointments.map {
            Matcher.all(
                haveComponent(
                    Link(
                        "editAppointmentLink",
                        EditAppointmentPage,
                        russianTimeFormat.format(it.wallClockDateTime) + " " + it.clientRef.resolveOrThrow().fullName()
                    )
                )
            )
        }

        return Matcher.all(*matchers.toTypedArray())
    }

}

object PastSchedulePage : SchedulePage(PastSchedulePageTab, "$BASE_PATH?past=true") {

    fun rowsFor(appointments: List<Appointment>): Matcher<Element> {
        val matchers = appointments.map {
            Matcher.all(
                haveComponent(
                    Link(
                        "editAppointmentLink",
                        EditAppointmentPage,
                        russianTimeFormat.format(it.wallClockDateTime) + " " + it.clientRef.resolveOrThrow().fullName()
                    )
                )
            )
        }

        return Matcher.all(*matchers.toTypedArray())
    }

}