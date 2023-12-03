package pro.qyoga.assertions

import io.kotest.matchers.shouldBe
import pro.qyoga.core.clients.api.ClientCardDto
import pro.qyoga.core.clients.internal.Client


infix fun Client.shouldMatch(clientCardDto: ClientCardDto) {
    firstName shouldBe clientCardDto.firstName
    lastName shouldBe clientCardDto.lastName
    middleName shouldBe clientCardDto.middleName
    birthDate shouldBe clientCardDto.birthDate
    phoneNumber shouldBe clientCardDto.phoneNumber
    email shouldBe clientCardDto.email
    address shouldBe clientCardDto.address
    distributionSource shouldBe clientCardDto.distributionSource
    complaints shouldBe clientCardDto.complaints
}