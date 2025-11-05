package pro.qyoga.tests.pages.therapist.clients.card

import io.kotest.matchers.shouldBe
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.model.DistributionSourceType
import pro.qyoga.core.clients.cards.model.toUIFormat
import pro.qyoga.l10n.russianDateFormat
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.assertions.SelectorOnlyComponent
import pro.qyoga.tests.assertions.shouldBeElement
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.platform.html.*
import pro.qyoga.tests.platform.html.Input.Companion.email
import pro.qyoga.tests.platform.html.Input.Companion.hidden
import pro.qyoga.tests.platform.html.Input.Companion.tel
import pro.qyoga.tests.platform.html.Input.Companion.text
import java.util.*

abstract class ClientForm(action: FormAction) : QYogaForm("createClientForm", action) {

    val firstName by component { text("firstName", true) }
    val lastName by component { text("lastName", true) }
    val middleName by component { text("middleName", false) }
    val birthDate by component { text("birthDate", false) }
    val phoneNumber by component { tel("phoneNumber", true) }
    val email by component { email("email", false) }
    val address by component { text("address", false) }
    val complaints by component { TextArea("complaints", false) }
    val anamnesis by component { TextArea("anamnesis", false) }
    val distributionSourceType by component {
        Select("distributionSourceType", false, DistributionSourceType.entries.map { Option.of(it) })
    }
    val distributionSourceComment by component { text("distributionSourceComment", false) }
    val version by component { hidden("version", false) }
    val submit by component { Button("confirmButton", "Сохранить") }

    val invalidPhoneInput = "${phoneNumber.selector()}.is-invalid"

    @Suppress("unused") // верифицируется через components
    val duplicatedPhoneErrorMessage by component { SelectorOnlyComponent("#duplicatedPhoneErrorMessage") }

    object FormDraftScript : Script("formDraft") {
        val clientId = Variable("clientId")
        val serverState = Variable("serverState")
        override val vars = listOf(clientId, serverState)
    }

}

object CreateClientForm : ClientForm(FormAction.hxPost("/therapist/clients/create"))

object EditClientForm : ClientForm(FormAction.hxPost("/therapist/clients/{id}")) {

    fun clientForm(client: Client): PageMatcher = PageMatcher { element ->
        element.select(this.selector()).single() shouldBeElement action
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
        FormDraftScript.clientId.value(
            element.select(FormDraftScript.selector()).single(),
            UUID::class
        ) shouldBe client.id
        FormDraftScript.serverState.value(
            element.select(FormDraftScript.selector()).single(),
            ClientCardDto::class
        )!! shouldMatch client
    }

}
