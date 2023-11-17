package pro.qyoga.clients.pages.therapist.clients

import io.kotest.matchers.shouldBe
import org.jsoup.nodes.Element
import pro.qyoga.assertions.shouldBeElement
import pro.qyoga.infra.html.FormAction
import pro.qyoga.infra.html.Input
import pro.qyoga.infra.html.Input.Companion.text
import pro.qyoga.infra.html.QYogaForm
import pro.qyoga.infra.html.QYogaPage

object CreateClientPage : QYogaPage {

    override val path = "/therapist/clients/create"

    override val title = "Создать клиента"

    object CreateClientForm : QYogaForm("createClientForm", FormAction.hxPost(path)) {

        val firstName = text("firstName")
        val lastName = text("lastName")
        val middleName = text("middleName")
        val birthDate = text("birthDate") //?)
        val phoneNumber = text("phoneNumber")
        val email = text("email")
        val areaOfResidence = text("areaOfResidence")
        val distributionSource = text("distributionSource")
        val complains = text("complains")

        override val components: List<Input> = listOf(
            firstName,
            lastName,
            middleName,
            birthDate,
            phoneNumber,
            email,
            areaOfResidence,
            distributionSource,
            complains
        )

    }

    override fun match(element: Element) {
        element.select("title").text() shouldBe title

        element.getElementById(CreateClientForm.id)!! shouldBeElement CreateClientForm
    }

}