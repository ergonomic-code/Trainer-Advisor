package pro.qyoga.tests.pages.therapist.appointments

import io.kotest.inspectors.forAll
import io.kotest.matchers.Matcher
import io.kotest.matchers.collections.shouldBeSameSizeAs
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.core.appointments.core.Appointment
import pro.qyoga.l10n.russianTimeFormat
import pro.qyoga.tests.assertions.*
import pro.qyoga.tests.platform.html.*

private const val BASE_PATH = "/therapist/schedule"

abstract class SchedulePage(val content: Component, override val path: String = BASE_PATH) : HtmlPage {

    final override val title = "Расписание"

    val addAppointmentLink =
        Link("addAppointmentLink", CreateAppointmentPage, "")

    override val matcher = Matcher.all(
        haveTitle(title),
        haveComponent(addAppointmentLink),
        haveComponent(SchedulePageTabs),
        haveComponent(content)
    )

    companion object {
        val deleteButton = Button("deleteAppointmentButton", "", FormAction.hxDelete(EditAppointmentPage.path))
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

    private const val TODAY_APPOINTMENTS_ID = "todayAppointments"

    private const val NEXT_WEEK_APPOINTMENTS_ID = "nextWeekAppointments"

    private const val LATER_APPOINTMENTS_ID = "laterAppointments"

    const val TODAY_APPOINTMENT_ROWS = "#todayAppointments > div"

    override fun selector() = "#futureAppointments"

    override fun matcher() = Matcher.all(
        haveElements("#$TODAY_APPOINTMENTS_ID", 1),
        haveElements("#$NEXT_WEEK_APPOINTMENTS_ID", 1),
        haveElements("#$LATER_APPOINTMENTS_ID", 1),
    )

}

object PastSchedulePageTab : Component {

    override fun selector() = "#pastAppointments"

    override fun matcher(): Matcher<Element> {
        return alwaysSuccess()
    }

}

object FutureSchedulePage : SchedulePage(FutureSchedulePageTab)

object PastSchedulePage : SchedulePage(PastSchedulePageTab, "$BASE_PATH?past=true")

fun Document.todayAppointmentsRows(): Elements = this.select(FutureSchedulePageTab.TODAY_APPOINTMENT_ROWS)

fun Document.nextWeekAppointmentsRows(): Elements = this.select("#nextWeekAppointments div.appointment-row")

fun Document.laterAppointmentsRows(): Elements = this.select("#laterAppointments > div")

fun Document.pastAppointmentsRows(): Elements = this.select("#pastAppointments > div")

infix fun Elements.shouldMatch(appointments: Iterable<Appointment>) {
    this shouldBeSameSizeAs appointments
    this.zip(appointments).forAll { (el, app) ->
        el shouldHaveComponent Link(
            "editAppointmentLink",
            EditAppointmentPage,
            russianTimeFormat.format(app.wallClockDateTime) + " " + app.clientRef.resolveOrThrow().fullName()
        )

        el shouldHaveComponent SchedulePage.deleteButton
    }
}