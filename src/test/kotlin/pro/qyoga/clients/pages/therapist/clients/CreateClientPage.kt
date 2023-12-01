package pro.qyoga.clients.pages.therapist.clients

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.assertions.shouldBeElement
import pro.qyoga.infra.html.*
import pro.qyoga.infra.html.Input.Companion.email
import pro.qyoga.infra.html.Input.Companion.tel
import pro.qyoga.infra.html.Input.Companion.text

object CreateClientPage : QYogaPage {

    override val path = "/therapist/clients/create"

    override val title = "Новый клиент"

    object CreateClientForm : QYogaForm("createClientForm", FormAction.classicPost(path)) {

        val firstName = text("firstName")
        val lastName = text("lastName")
        val middleName = text("middleName")
        val birthDate = text("birthDate")
        val phoneNumber = tel("phoneNumber")
        val email = email("email")
        val address = text("address")
        val distributionSource = TextArea("distributionSource")
        val complains = TextArea("complaints")

        override val components: List<Component> = listOf(
            firstName,
            lastName,
            middleName,
            birthDate,
            phoneNumber,
            email,
            address,
            distributionSource,
            complains
        )

    }

    override fun match(element: Element) {
        element.select("title").text() shouldBe title

        element.getElementById(CreateClientForm.id)!! shouldBeElement CreateClientForm
    }

}