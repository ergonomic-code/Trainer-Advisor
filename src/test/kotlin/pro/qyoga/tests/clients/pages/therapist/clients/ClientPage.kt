package pro.qyoga.tests.clients.pages.therapist.clients

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import org.jsoup.nodes.Element
import pro.qyoga.core.clients.api.Client
import pro.qyoga.core.clients.api.DistributionSourceType
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.shouldBeElement
import pro.qyoga.tests.infra.html.*
import pro.qyoga.tests.infra.html.Input.Companion.email
import pro.qyoga.tests.infra.html.Input.Companion.tel
import pro.qyoga.tests.infra.html.Input.Companion.text
import java.time.format.DateTimeFormatter

abstract class ClientPage(action: FormAction) : QYogaPage {

    private val birthDateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    val clientForm = ClientForm(action)

    override fun match(element: Element) {
        element.select("title").text() shouldMatch title

        element.getElementById(clientForm.id)!! shouldBeElement clientForm
    }

    fun clientForm(client: Client): PageMatcher = object : PageMatcher {

        override fun match(element: Element) {
            element.select(clientForm.firstName.selector()).`val`() shouldBe client.firstName
            element.select(clientForm.lastName.selector()).`val`() shouldBe client.lastName
            element.select(clientForm.middleName.selector()).`val`() shouldBe (client.middleName ?: "")
            element.select(clientForm.birthDate.selector()).`val`() shouldBe client.birthDate.format(birthDateFormat)
            element.select(clientForm.phoneNumber.selector()).`val`() shouldBe client.phoneNumber
            element.select(clientForm.email.selector()).`val`() shouldBe (client.email ?: "")
            element.select(clientForm.address.selector()).`val`() shouldBe (client.address ?: "")
            element.select(clientForm.complaints.selector()).text() shouldBe client.complaints
            element.select(clientForm.anamnesis.selector()).text() shouldBe client.anamnesis
            element.select(clientForm.distributionSourceType.selector())
                .`val`() shouldBe (client.distributionSource?.type?.name ?: "")
            element.select(clientForm.distributionSourceComment.selector())
                .`val`() shouldBe (client.distributionSource?.comment ?: "")
        }

    }

}

private const val CREATE_CLIENT_PATH = "/therapist/clients/create"

object CreateClientPage : ClientPage(FormAction.classicPost(CREATE_CLIENT_PATH)) {

    override val path = CREATE_CLIENT_PATH

    override val title = "Новый клиент"

}

private const val EDIT_CLIENT_PATH = "/therapist/clients/{id}"

class EditClientPage(clientId: Long) :
    ClientPage(FormAction.classicPost(EDIT_CLIENT_PATH.replace("{id}", clientId.toString()))) {

    override val path = EDIT_CLIENT_PATH

    override val title = ".*"

}

class ClientForm(action: FormAction) : QYogaForm("createClientForm", action) {

    val firstName = text("firstName", true)
    val lastName = text("lastName", true)
    val middleName = text("middleName", false)
    val birthDate = text("birthDate", false)
    val phoneNumber = tel("phoneNumber", true)
    val email = email("email", false)
    val address = text("address", false)
    val complaints = TextArea("complaints", true)
    val anamnesis = TextArea("anamnesis", false)
    val distributionSourceType =
        Select("distributionSourceType", false, DistributionSourceType.entries.map { Option.of(it) })
    val distributionSourceComment = text("distributionSourceComment", false)

    override val components: List<Component> = listOf(
        firstName,
        lastName,
        middleName,
        birthDate,
        phoneNumber,
        email,
        address,
        complaints,
        anamnesis,
        distributionSourceType,
        distributionSourceComment,
    )

}
