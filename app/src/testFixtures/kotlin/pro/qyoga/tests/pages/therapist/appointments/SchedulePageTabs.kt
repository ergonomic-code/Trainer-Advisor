package pro.qyoga.tests.pages.therapist.appointments

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.platform.html.Component
import pro.qyoga.tests.platform.html.Link


object SchedulePageTabs : Component {

    // lazy - для обхода цикла наследники SchedulePage -> SchedulePageTabs -> наследники SchedulePage
    // не уверен, что этот цикл в коде надо разрывать, так как он внутри одного пакета и это фактический цикл в UI-е
    private val futureAppointmentsLink by lazy {
        Link(
            "futureAppointmentsLink",
            FutureSchedulePage.path,
            "Предстоящие"
        )
    }
    private val pastAppointmentsLink by lazy { Link("pastAppointmentsLink", PastSchedulePage.path, "Прошедшие") }

    override fun selector() = "#scheduleTabs"

    override fun matcher(): Matcher<Element> {
        return Matcher.all(
            haveComponent(futureAppointmentsLink),
            haveComponent(pastAppointmentsLink)
        )
    }

}