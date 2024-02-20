package pro.qyoga.tests.pages.therapist.appointments

import io.kotest.inspectors.forAll
import io.kotest.matchers.Matcher
import io.kotest.matchers.collections.shouldBeSameSizeAs
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.app.therapist.appointments.core.edit.CreateAppointmentPageController
import pro.qyoga.app.therapist.appointments.core.schedule.CalendarPageModel
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageController
import pro.qyoga.app.therapist.appointments.core.schedule.TimeMark
import pro.qyoga.core.appointments.core.Appointment
import pro.qyoga.l10n.russianTimeFormat
import pro.qyoga.tests.assertions.*
import pro.qyoga.tests.pages.therapist.appointments.CalendarPage.APPOINTMENT_CARD_SELECTOR
import pro.qyoga.tests.platform.html.Button
import pro.qyoga.tests.platform.html.HtmlPage
import pro.qyoga.tests.platform.html.Link
import java.time.LocalTime


object CalendarPage : HtmlPage {

    private val datePickerButton = Button("datePickerButton", "")

    private val goToDayLink = Link("goToDayLink-", SchedulePageController.DATE_PATH, "")

    val addAppointmentLink = Link("addAppointmentLink-", CreateAppointmentPageController.ADD_TO_DATE_PATH, "")

    const val APPOINTMENT_CARD_SELECTOR = ".appointment-card"

    override val matcher = Matcher.all(
        haveComponent(datePickerButton),
        haveElement("small:contains(07:00)"),
        haveComponents(goToDayLink, CalendarPageModel.DAYS_IN_WEEK),
        haveAtLeastElements(
            addAppointmentLink,
            CalendarPageModel.DAYS_IN_CALENDAR * TimeMark.marksPerHour * (CalendarPageModel.DEFAULT_END_HOUR - CalendarPageModel.DEFAULT_START_HOUR)
        )
    )

    override val path = SchedulePageController.PATH

    override val title = "Расписание"

}

fun Document.appointmentCards(): Elements = this.select(APPOINTMENT_CARD_SELECTOR)


infix fun Elements.shouldMatch(appointments: Iterable<Appointment>) {
    this shouldBeSameSizeAs appointments

    val timeAndDateComparator = Comparator.comparing<Appointment, LocalTime> { it.wallClockDateTime.toLocalTime() }
        .thenComparing { it -> it.wallClockDateTime.toLocalDate() }
    val appointmentsInHtmlOrder = appointments.sortedWith(timeAndDateComparator)

    this.zip(appointmentsInHtmlOrder).forAll { (el, app) ->
        el shouldHaveComponent Link(
            "editAppointmentLink",
            EditAppointmentPage,
            russianTimeFormat.format(app.wallClockDateTime) + " - " + russianTimeFormat.format(app.endWallClockDateTime) + " " + app.clientRef.resolveOrThrow()
                .fullName()
        )
    }
}