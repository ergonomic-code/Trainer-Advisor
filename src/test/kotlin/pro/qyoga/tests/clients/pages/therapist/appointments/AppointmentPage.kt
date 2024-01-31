package pro.qyoga.tests.clients.pages.therapist.appointments

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.assertions.haveTitle
import pro.qyoga.tests.infra.html.HtmlPage


abstract class AppointmentsPage(
    final override val title: String,
    override val path: String,
    val editAppointmentForm: AppointmentForm
) : HtmlPage {

    override val matcher = Matcher.all(
        haveTitle(title),
        haveComponent(editAppointmentForm)
    )

}

object CreateAppointmentPage : AppointmentsPage("Новый приём", CreateAppointmentForm.action.url, CreateAppointmentForm)