package pro.qyoga.tests.clients.pages.therapist.appointments

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.assertions.haveTitle
import pro.qyoga.tests.infra.html.HtmlPage
import pro.qyoga.tests.infra.html.Link


object SchedulePage : HtmlPage {

    override val path = "/therapist/schedule"

    override val title = "Расписание"

    private val addTrainingSessionLink =
        Link("addTrainingSessionLink", CreateAppointmentPage, "Запланировать приём")

    override val matcher = Matcher.all(
        haveTitle(title),
        haveComponent(addTrainingSessionLink)
    )

}