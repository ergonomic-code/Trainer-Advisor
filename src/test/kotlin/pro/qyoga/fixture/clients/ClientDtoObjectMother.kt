package pro.qyoga.fixture.clients

import pro.qyoga.core.clients.api.ClientDto
import pro.qyoga.core.clients.api.CreateClientRequest
import pro.qyoga.fixture.data.randomCyrillicWord
import pro.qyoga.fixture.data.randomEmail
import pro.qyoga.fixture.data.randomLocalDate
import java.time.Duration
import java.time.LocalDate
import kotlin.random.Random


object ClientDtoObjectMother {

    fun createClientDto(
        firstName: String = randomCyrillicWord(),
        lastName: String = randomCyrillicWord(),
        patronymic: String = randomCyrillicWord(),
        localDate: LocalDate = randomBirthDate(),
        phone: String = randomPhoneNumber(),
        email: String = randomEmail(),
        areaOfResidence: String = randomCyrillicWord(),
        distributionSource: String = randomCyrillicWord(),
        complains: String = randomCyrillicWord(),
    ): ClientDto =
        ClientDto(
            firstName,
            lastName,
            patronymic,
            localDate,
            phone,
            email,
            areaOfResidence,
            distributionSource,
            complains
        )

    fun createClientDtos(count: Int): List<ClientDto> =
        (1..count).map { createClientDto() }

    fun createClientRequest(
        firstName: String = randomCyrillicWord(),
        lastName: String = randomCyrillicWord(),
        patronymic: String = randomCyrillicWord(),
        localDate: LocalDate = randomBirthDate(),
        phone: String = randomPhoneNumber(),
        email: String = randomEmail(),
        areaOfResidence: String = randomCyrillicWord(),
        distributionSource: String = randomCyrillicWord(),
        complains: String = randomCyrillicWord(),
    ): CreateClientRequest = CreateClientRequest(
        firstName,
        lastName,
        patronymic,
        localDate,
        phone,
        email,
        areaOfResidence,
        distributionSource,
        complains
    )

    fun createClientDto(createClientRequest: CreateClientRequest):
            ClientDto = ClientDto(
        createClientRequest.firstName,
        createClientRequest.lastName,
        createClientRequest.middleName,
        createClientRequest.birthDate,
        createClientRequest.phoneNumber,
        createClientRequest.email,
        createClientRequest.areaOfResidence,
        createClientRequest.distributionSource,
        createClientRequest.complains
    )
}

val minBirthDate: LocalDate = LocalDate.ofYearDay(1960, 1)
const val MIN_AGE = 6L
val maxBirthDate: LocalDate = LocalDate.now().minusDays(MIN_AGE * 365)

fun randomBirthDate(): LocalDate =
    randomLocalDate(minBirthDate, Duration.between(minBirthDate.atStartOfDay(), maxBirthDate.atStartOfDay()))

fun randomPhoneNumber() =
    "+7-${Random.nextInt(900, 999)}-${Random.nextInt(100, 999)}-${Random.nextInt(10, 99)}-${Random.nextInt(10, 99)}"
