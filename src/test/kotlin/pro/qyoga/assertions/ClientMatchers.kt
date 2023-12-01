package pro.qyoga.assertions

import io.kotest.matchers.shouldBe
import pro.qyoga.core.clients.api.CreateClientRequest
import pro.qyoga.core.clients.internal.Client


infix fun Client.shouldMatch(createClientRequest: CreateClientRequest) {
    this.firstName shouldBe createClientRequest.firstName
    lastName shouldBe createClientRequest.lastName
    middleName shouldBe createClientRequest.middleName
    birthDate shouldBe createClientRequest.birthDate
    phoneNumber shouldBe createClientRequest.phoneNumber
    email shouldBe createClientRequest.email
    address shouldBe createClientRequest.address
    distributionSource shouldBe createClientRequest.distributionSource
    complaints shouldBe createClientRequest.complaints
}