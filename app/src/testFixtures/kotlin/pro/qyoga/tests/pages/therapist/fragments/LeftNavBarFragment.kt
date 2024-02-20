package pro.qyoga.tests.pages.therapist.fragments

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.haveComponent
import pro.qyoga.tests.pages.therapist.appointments.EmptyFutureSchedulePage
import pro.qyoga.tests.pages.therapist.clients.ClientsListPage
import pro.qyoga.tests.pages.therapist.therapy.exercises.ExercisesListPage
import pro.qyoga.tests.pages.therapist.therapy.programs.ProgramsListPage
import pro.qyoga.tests.pages.therapist.therapy.therapeutic_tasks.TherapeuticTasksListPage
import pro.qyoga.tests.platform.html.Component
import pro.qyoga.tests.platform.html.Link

object LeftNavBarFragment : Component {

    override fun selector() = "#layoutSidenav_nav"

    // Work
    private val scheduleLink = Link("schedulePageLink", EmptyFutureSchedulePage, "Расписание")
    private val clientsLink = Link("clientsPageLink", ClientsListPage, "Клиенты")
    private val programsLink = Link("programsPageLink", ProgramsListPage, "Программы")

    // Dictionaries
    private val exercisesLink = Link("exercisesPageLink", ExercisesListPage, "Упражнения")
    private val therapeuticTasksLink =
        Link("therapeuticTasksPageLink", TherapeuticTasksListPage, "Терапевтические задачи")

    // Account
    private val logoutLink = Link("logoutLink", "/logout", "Выйти")

    override fun matcher(): Matcher<Element> =
        Matcher.all(
            haveComponent(scheduleLink),
            haveComponent(clientsLink),
            haveComponent(programsLink),

            haveComponent(exercisesLink),
            haveComponent(therapeuticTasksLink),

            haveComponent(logoutLink)
        )

}