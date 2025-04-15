package pro.qyoga.tests.assertions

import io.kotest.matchers.shouldBe
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.model.PhoneNumber
import pro.qyoga.core.clients.cards.model.toE164Format
import pro.qyoga.core.clients.cards.model.toUIFormat


infix fun Client.shouldMatch(clientCardDto: ClientCardDto) {
    firstName shouldBe clientCardDto.firstName
    lastName shouldBe clientCardDto.lastName
    middleName shouldBe clientCardDto.middleName
    birthDate shouldBe clientCardDto.birthDate
    phoneNumber.toE164Format() shouldBe PhoneNumber.of(clientCardDto.phoneNumber).toE164Format()
    email shouldBe clientCardDto.email
    address shouldBe clientCardDto.address
    distributionSource?.type shouldBe clientCardDto.distributionSourceType
    distributionSource?.comment shouldBe clientCardDto.distributionSourceComment
    complaints shouldBe clientCardDto.complaints
    anamnesis shouldBe clientCardDto.anamnesis
}

infix fun ClientCardDto.shouldMatch(client: Client) {
    firstName shouldBe client.firstName
    lastName shouldBe client.lastName
    middleName shouldBe client.middleName
    birthDate shouldBe client.birthDate
    phoneNumber shouldBe client.phoneNumber.toUIFormat()
    email shouldBe client.email
    address shouldBe client.address
    distributionSource?.type shouldBe client.distributionSource?.type
    distributionSource?.comment shouldBe client.distributionSource?.comment
    complaints shouldBe client.complaints
    anamnesis shouldBe client.anamnesis
}