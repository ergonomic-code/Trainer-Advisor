package pro.qyoga.tests.pages.therapist.appointments

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import pro.qyoga.app.therapist.appointments.core.edit.EditAppointmentPageController
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.assertions.haveTitle
import pro.qyoga.tests.platform.html.HtmlPage
import pro.qyoga.tests.platform.html.Link
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

object EditAppointmentPage : AppointmentsPage(EditAppointmentForm) {

    private val deleteLink =
        Link("deleteAppointmentLink", EditAppointmentPageController.DELETE_PATH, "", targetAttr = "hx-delete")

    override val matcher = Matcher.all(
        super.matcher,
        haveComponent(deleteLink)
    )

}