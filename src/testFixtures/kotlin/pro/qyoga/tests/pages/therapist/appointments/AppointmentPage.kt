package pro.qyoga.tests.pages.therapist.appointments

import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.assertions.haveTitle
import pro.qyoga.tests.platform.html.HtmlPage
import pro.qyoga.tests.platform.kotest.buildAllOfMatcher


abstract class AppointmentsPage(
    val editAppointmentForm: AppointmentForm,
    final override val title: String? = null,
) : HtmlPage {

    override val path = editAppointmentForm.action.url

    override val matcher = buildAllOfMatcher {
        if (title != null) {
            add(haveTitle(title))
        }
        add(haveComponent(editAppointmentForm))
    }

}

object CreateAppointmentPage : AppointmentsPage(CreateAppointmentForm, "Новый приём")

object EditAppointmentPage : AppointmentsPage(EditAppointmentForm)