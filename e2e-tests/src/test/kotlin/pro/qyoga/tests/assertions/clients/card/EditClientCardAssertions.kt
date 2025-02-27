package pro.qyoga.tests.assertions.clients.card

import io.kotest.matchers.shouldBe
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.model.toUIFormat
import pro.qyoga.l10n.russianDateFormat
import pro.qyoga.tests.assertions.shouldBeEmptyInput
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientForm
import pro.qyoga.tests.pages.therapist.clients.card.EditClientForm
import pro.qyoga.tests.platform.html.InputBase
import pro.qyoga.tests.platform.selenide.find

fun CreateClientForm.shouldBeEmpty() {
    components
        .filterIsInstance<InputBase>()
        .forEach { component ->
            find(component).shouldBeEmptyInput()
        }
}

fun currentPageShouldBeFormFor(client: Client) {
    find(EditClientForm.lastName).`val`() shouldBe client.lastName
    find(EditClientForm.firstName).`val`() shouldBe client.firstName
    find(EditClientForm.middleName).`val`() shouldBe client.middleName
    find(EditClientForm.phoneNumber).`val`() shouldBe client.phoneNumber.toUIFormat()
    find(EditClientForm.birthDate).`val`() shouldBe client.birthDate?.format(russianDateFormat)
    find(EditClientForm.email).`val`() shouldBe client.email
    find(EditClientForm.address).`val`() shouldBe client.address
    find(EditClientForm.distributionSourceType).`val`() shouldBe client.distributionSource?.type?.name
    find(EditClientForm.distributionSourceComment).`val`() shouldBe client.distributionSource?.comment
    find(EditClientForm.complaints).`val`() shouldBe client.complaints
    find(EditClientForm.anamnesis).`val`() shouldBe client.anamnesis
}