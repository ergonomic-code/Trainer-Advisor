package pro.qyoga.tests.scripts.therapist.clients.card

import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.model.PhoneNumber
import pro.qyoga.l10n.russianDateFormat
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientForm
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientPage
import pro.qyoga.tests.platform.selenide.open
import pro.qyoga.tests.platform.selenide.selectIn
import pro.qyoga.tests.platform.selenide.typeInto

fun goToCreateClientPage() {
    open(CreateClientPage)
}

fun fillClientForm(client: ClientCardDto) {
    typeInto(CreateClientForm.firstName, client.firstName)
    typeInto(CreateClientForm.lastName, client.lastName)
    typeInto(CreateClientForm.middleName, client.middleName)
    typeInto(CreateClientForm.birthDate, client.birthDate?.format(russianDateFormat))
    typeInto(CreateClientForm.phoneNumber, PhoneNumber.of(client.phoneNumber).nationalNumber)
    typeInto(CreateClientForm.email, client.email)
    typeInto(CreateClientForm.address, client.address)
    typeInto(CreateClientForm.complaints, client.complaints)
    typeInto(CreateClientForm.anamnesis, client.anamnesis)
    selectIn(CreateClientForm.distributionSourceType, client.distributionSourceType)
    typeInto(CreateClientForm.distributionSourceComment, client.distributionSourceComment)
}
