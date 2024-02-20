package pro.qyoga.tests.assertions

import io.kotest.matchers.shouldBe
import pro.qyoga.core.clients.cards.api.Client
import pro.qyoga.core.clients.cards.api.ClientCardDto


infix fun Client.shouldMatch(clientCardDto: ClientCardDto) {
    firstName shouldBe clientCardDto.firstName
    lastName shouldBe clientCardDto.lastName
    middleName shouldBe clientCardDto.middleName
    birthDate shouldBe clientCardDto.birthDate
    phoneNumber shouldBe clientCardDto.phoneNumber
    email shouldBe clientCardDto.email
    address shouldBe clientCardDto.address
    distributionSource?.type shouldBe clientCardDto.distributionSourceType
    distributionSource?.comment shouldBe clientCardDto.distributionSourceComment
    complaints shouldBe clientCardDto.complaints
    anamnesis shouldBe clientCardDto.anamnesis
}