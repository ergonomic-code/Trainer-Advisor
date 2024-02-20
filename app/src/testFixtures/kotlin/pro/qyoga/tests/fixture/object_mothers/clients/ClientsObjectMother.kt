package pro.qyoga.tests.fixture.object_mothers.clients

import org.springframework.data.jdbc.core.mapping.AggregateReference
import pro.qyoga.core.clients.cards.api.ClientCardDto
import pro.qyoga.core.clients.cards.api.ClientRef
import pro.qyoga.core.clients.cards.api.DistributionSource
import pro.qyoga.core.clients.cards.api.DistributionSourceType
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.data.randomEmail
import pro.qyoga.tests.fixture.data.randomLocalDate
import pro.qyoga.tests.fixture.data.randomSentence
import java.time.Duration
import java.time.LocalDate
import kotlin.random.Random


object ClientsObjectMother {

    fun createClientCardDtos(count: Int): List<ClientCardDto> =
        (1..count).map { createClientCardDto() }

    fun createClientCardDto(
        firstName: String = randomCyrillicWord(),
        lastName: String = randomCyrillicWord(),
        middleName: String? = randomCyrillicWord(),
        birthDate: LocalDate = randomBirthDate(),
        phone: String = randomPhoneNumber(),
        email: String? = randomEmail(),
        address: String? = randomCyrillicWord(),
        complains: String = randomCyrillicWord(),
        anamnesis: String? = randomSentence(),
        distributionSource: DistributionSource = randomDistributionSource(),
    ): ClientCardDto = createClientCardDtoMinimal(
        firstName,
        lastName,
        middleName,
        birthDate,
        phone,
        email,
        address,
        complains,
        anamnesis,
        distributionSource,
    )

    fun createClientCardDtoMinimal(
        firstName: String = randomCyrillicWord(),
        lastName: String = randomCyrillicWord(),
        middleName: String? = null,
        birthDate: LocalDate? = null,
        phone: String = randomPhoneNumber(),
        email: String? = null,
        address: String? = null,
        complains: String? = null,
        anamnesis: String? = null,
        distributionSource: DistributionSource? = null
    ): ClientCardDto = ClientCardDto(
        firstName,
        lastName,
        middleName,
        birthDate,
        phone,
        email,
        address,
        complains,
        anamnesis,
        distributionSource?.type,
        distributionSource?.comment,
    )

    val fakeClientRef: ClientRef = AggregateReference.to(-1)

}

val minBirthDate: LocalDate = LocalDate.ofYearDay(1960, 1)
const val MIN_AGE = 6L
val maxBirthDate: LocalDate = LocalDate.now().minusDays(MIN_AGE * 365)

fun randomBirthDate(): LocalDate =
    randomLocalDate(minBirthDate, Duration.between(minBirthDate.atStartOfDay(), maxBirthDate.atStartOfDay()))

fun randomPhoneNumber() =
    "+7-${Random.nextInt(900, 999)}-${Random.nextInt(100, 999)}-${Random.nextInt(10, 99)}-${Random.nextInt(10, 99)}"

fun randomDistributionSource(): DistributionSource {
    val type = DistributionSourceType.entries.random()
    val hasComment = Random.nextBoolean()
    val comment = if (hasComment) randomSentence(1, 5) else null
    return DistributionSource(type, comment)
}