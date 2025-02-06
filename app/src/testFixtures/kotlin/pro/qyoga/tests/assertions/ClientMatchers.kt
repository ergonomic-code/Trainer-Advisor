package pro.qyoga.tests.assertions

import io.kotest.matchers.shouldBe
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.model.PhoneNumber
import pro.qyoga.core.clients.cards.model.toE164Format


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