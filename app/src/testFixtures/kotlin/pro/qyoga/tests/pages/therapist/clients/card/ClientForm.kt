package pro.qyoga.tests.pages.therapist.clients.card

import io.kotest.matchers.shouldBe
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.model.DistributionSourceType
import pro.qyoga.core.clients.cards.model.toUIFormat
import pro.qyoga.l10n.russianDateFormat
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.SelectorOnlyComponent
import pro.qyoga.tests.platform.html.*
import pro.qyoga.tests.platform.html.Input.Companion.email
import pro.qyoga.tests.platform.html.Input.Companion.hidden
import pro.qyoga.tests.platform.html.Input.Companion.tel
import pro.qyoga.tests.platform.html.Input.Companion.text

abstract class ClientForm(action: FormAction) : QYogaForm("createClientForm", action) {

    val firstName = text("firstName", true)
    val lastName = text("lastName", true)
    val middleName = text("middleName", false)
    val birthDate = text("birthDate", false)
    val phoneNumber = tel("phoneNumber", true)
    val invalidPhoneInput = "${phoneNumber.selector()}.is-invalid"
    val duplicatedPhoneErrorMessage = "#duplicatedPhoneErrorMessage"
    val email = email("email", false)
    val address = text("address", false)
    val complaints = TextArea("complaints", false)
    val anamnesis = TextArea("anamnesis", false)
    val distributionSourceType =
        Select("distributionSourceType", false, DistributionSourceType.entries.map { Option.of(it) })
    val distributionSourceComment = text("distributionSourceComment", false)
    val version = hidden("version", false)
    val submit = Button("confirmButton", "Сохранить")

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
        SelectorOnlyComponent(duplicatedPhoneErrorMessage),
        submit
    )

}

object CreateClientForm : ClientForm(FormAction.classicPost("/therapist/clients/create"))

object EditClientForm : ClientForm(FormAction.classicPost("/therapist/clients/{id}")) {

    fun clientForm(client: Client): PageMatcher = PageMatcher { element ->
        element.select(firstName.selector()).`val`() shouldBe client.firstName
        element.select(lastName.selector()).`val`() shouldBe client.lastName
        element.select(middleName.selector()).`val`() shouldBe (client.middleName ?: "")
        element.select(birthDate.selector()).`val`() shouldBe (client.birthDate?.format(russianDateFormat) ?: "")
        element.select(phoneNumber.selector()).`val`() shouldBe client.phoneNumber.toUIFormat()
        element.select(email.selector()).`val`() shouldBe (client.email ?: "")
        element.select(address.selector()).`val`() shouldBe (client.address ?: "")
        element.select(distributionSourceType.selector())
            .`val`() shouldBe (client.distributionSource?.type?.name ?: "")
        element.select(distributionSourceComment.selector())
            .`val`() shouldBe (client.distributionSource?.comment ?: "")
        element.select(complaints.selector()).text() shouldBe (client.complaints ?: "")
    }

}
