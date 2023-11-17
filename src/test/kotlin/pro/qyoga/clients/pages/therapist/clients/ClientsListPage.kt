package pro.qyoga.clients.pages.therapist.clients

import io.kotest.inspectors.forAny
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import pro.qyoga.assertions.PageMatcher
import pro.qyoga.assertions.shouldBeElement
import pro.qyoga.core.clients.api.ClientDto
import pro.qyoga.infra.html.FormAction
import pro.qyoga.infra.html.Input
import pro.qyoga.infra.html.Input.Companion.text
import pro.qyoga.infra.html.QYogaForm
import pro.qyoga.infra.html.QYogaPage


object ClientsListPage : QYogaPage {

    override val path = "/therapist/clients"

    override val title = "Список клиентов"

    object ClientSearchForm : QYogaForm("clientSearch", FormAction.hxGet("$path/search")) {

        val lastName = text("lastName")
        val firstName = text("firstName")
        val patronymic = text("patronymic")
        val phoneNumber = text("phoneNumber")

        override val components: List<Input> = listOf(
            lastName,
            firstName,
            patronymic,
            phoneNumber
        )

    }

    val deleteAction = "$path/delete/{id}"
    val deleteActionPattern = deleteAction.replace("{id}", "(\\d+)").toRegex()


    private const val CLIENT_ROW = "tbody tr"

    override fun match(element: Element) {
        element.select("title").text() shouldBe title

        element.getElementById(ClientSearchForm.id)!! shouldBeElement ClientSearchForm
    }

    fun clientId(document: Document, rowIdx: Int): Long {
        val deleteUrl = document.select(CLIENT_ROW)[rowIdx].select("td button").attr("hx-delete")
        val matcher = deleteActionPattern.matchEntire(deleteUrl)
        checkNotNull(matcher)
        return matcher.groups[1]!!.value.toLong()
    }

    fun clientRow(client: ClientDto): PageMatcher = object : PageMatcher {
        override fun match(element: Element) {
            element.select(CLIENT_ROW).forAny { row ->
                row.select("td:nth-child(1)").text() shouldBe client.lastName
                row.select("td:nth-child(2)").text() shouldBe client.firstName
                row.select("td:nth-child(3)").text() shouldBe client.middleName
                row.select("td button").attr("hx-delete") shouldMatch deleteActionPattern
            }
        }
    }

    fun clientRows(document: Document): Elements =
        document.select(CLIENT_ROW)

}