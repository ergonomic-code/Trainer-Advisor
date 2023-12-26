package pro.qyoga.tests.clients.pages.therapist.fragments

import org.jsoup.nodes.Element
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.clients.pages.therapist.clients.ClientsListPage
import pro.qyoga.tests.clients.pages.therapist.therapy.exercises.ExercisesListPage
import pro.qyoga.tests.clients.pages.therapist.therapy.therapeutic_tasks.TherapeuticTasksListPage
import pro.qyoga.tests.infra.html.Component
import pro.qyoga.tests.infra.html.Link

object LeftNavBarFragment : Component {

    override fun selector() = "#layoutSidenav_nav"

    private val clientsLink = Link("clientsPageLink", ClientsListPage, "Клиенты")
    private val exercisesLink = Link("exercisesPageLink", ExercisesListPage, "Упражнения")

    private val therapeuticTasksLink =
        Link("therapeuticTasksPageLink", TherapeuticTasksListPage, "Терапевтические задачи")

    private val logoutLink = Link("logoutLink", "/logout", "Выйти")

    override fun match(element: Element) {
        element shouldHaveComponent clientsLink
        element shouldHaveComponent exercisesLink

        element shouldHaveComponent therapeuticTasksLink

        element shouldHaveComponent logoutLink
    }

}