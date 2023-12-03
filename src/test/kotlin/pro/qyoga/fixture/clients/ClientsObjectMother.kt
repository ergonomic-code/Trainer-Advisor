package pro.qyoga.fixture.clients

import pro.qyoga.core.clients.api.ClientCardDto
import pro.qyoga.fixture.data.randomCyrillicWord
import pro.qyoga.fixture.data.randomEmail
import pro.qyoga.fixture.data.randomLocalDate
import java.time.Duration
import java.time.LocalDate
import kotlin.random.Random


object ClientsObjectMother {

    fun createClientCardDtos(count: Int): List<ClientCardDto> =
        (1..count).map { createClientCardDto() }

    fun createClientCardDto(
        firstName: String = randomCyrillicWord(),
        lastName: String = randomCyrillicWord(),
        middleName: String = randomCyrillicWord(),
        localDate: LocalDate = randomBirthDate(),
        phone: String = randomPhoneNumber(),
        email: String = randomEmail(),
        address: String = randomCyrillicWord(),
        distributionSource: String = randomCyrillicWord(),
        complains: String = randomCyrillicWord(),
    ): ClientCardDto = ClientCardDto(
        firstName,
        lastName,
        middleName,
        localDate,
        phone,
        email,
        address,
        distributionSource,
        complains
    )

}

val minBirthDate: LocalDate = LocalDate.ofYearDay(1960, 1)
const val MIN_AGE = 6L
val maxBirthDate: LocalDate = LocalDate.now().minusDays(MIN_AGE * 365)

fun randomBirthDate(): LocalDate =
    randomLocalDate(minBirthDate, Duration.between(minBirthDate.atStartOfDay(), maxBirthDate.atStartOfDay()))

fun randomPhoneNumber() =
    "+7-${Random.nextInt(900, 999)}-${Random.nextInt(100, 999)}-${Random.nextInt(10, 99)}-${Random.nextInt(10, 99)}"
