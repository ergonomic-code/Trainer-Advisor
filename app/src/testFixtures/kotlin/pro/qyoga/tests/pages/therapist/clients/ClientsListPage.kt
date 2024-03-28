package pro.qyoga.tests.pages.therapist.clients

import io.kotest.inspectors.forAny
import io.kotest.matchers.Matcher
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import pro.qyoga.app.therapist.clients.ClientsListPageController
import pro.qyoga.core.clients.cards.api.Client
import pro.qyoga.tests.assertions.*
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientPage
import pro.qyoga.tests.platform.html.*
import pro.qyoga.tests.platform.html.Input.Companion.text
import pro.qyoga.tests.platform.kotest.buildAllOfMatcher


object ClientsListPage : QYogaPage {

    override val path = ClientsListPageController.PATH

    override val title = "Список клиентов"

    object ClientSearchForm :
        QYogaForm("searchClientsFilterForm", FormAction.hxGet(ClientsListPageController.SEARCH_PATH)) {

        val lastName = text(ClientsListPageController.SEARCH_PARAM_LAST_NAME, false)
        val firstName = text(ClientsListPageController.SEARCH_PARAM_FIRST_NAME, false)
        val phoneNumber = text(ClientsListPageController.SEARCH_PARAM_PHONE_NUMBER, false)

        override val components: List<Input> = listOf(
            lastName,
            firstName,
            phoneNumber
        )

    }

    val updateAction = "$path/{id}/journal"
    private val updateActionPattern = updateAction.replace("{id}", "(\\d+)").toRegex()

    val deleteAction = "$path/delete/{id}"
    private val deleteActionPattern = deleteAction.replace("{id}", "(\\d+)").toRegex()

    private val addLink = Link("createClientLink", CreateClientPage, "Добавить")

    private const val CLIENT_ROW = "tbody tr"

    override fun match(element: Element) {
        element.select("title").text() shouldBe title

        element.getElementById(ClientSearchForm.id)!! shouldBeComponent ClientSearchForm

        element shouldHaveComponent addLink
    }

    fun clientId(document: Document, rowIdx: Int): Long {
        val deleteUrl = document.select(CLIENT_ROW)[rowIdx].select("td a").attr("hx-delete")
        val matcher = deleteActionPattern.matchEntire(deleteUrl)
        checkNotNull(matcher)
        return matcher.groups[1]!!.value.toLong()
    }

    fun clientRow(client: Client): PageMatcher = PageMatcher { element ->
        element.select(CLIENT_ROW).forAny { row ->
            row.select("td.nameCell").text() shouldBe client.fullName()
            row.select("td a.updateLink").attr("href") shouldMatch updateActionPattern
            row.select("td a.deleteClientLink").attr("hx-delete") shouldMatch deleteActionPattern
        }
    }

    fun clientRows(document: Document): Elements =
        document.select(CLIENT_ROW)

}

class ClientsListPagination(val pages: Int, val currentPage: Int) : Component {

    private val prevPageLink = Link(
        "prevPageLink",
        ClientsListPageController.SEARCH_URL.replace("{page}", (currentPage - 2).toString()),
        "",
        targetAttr = "hx-get"
    )
    private val pageLink = Link("numberedPageLink", ClientsListPageController.SEARCH_URL, "", targetAttr = "hx-get")
    private val nextPageLink = Link(
        "nextPageLink",
        ClientsListPageController.SEARCH_URL.replace("{page}", currentPage.toString()),
        "",
        targetAttr = "hx-get"
    )

    override fun selector() = "#pagination"

    override fun matcher(): Matcher<Element> {
        return buildAllOfMatcher {
            if (pages == 0) {
                add(haveComponent(".pagination.page").invert())
                return@buildAllOfMatcher
            }
            if (currentPage > 1) {
                add(haveComponent(prevPageLink))
            }

            add(haveComponents(pageLink, pages))

            if (currentPage < pages) {
                add(haveComponent(nextPageLink))
            }
        }
    }

}