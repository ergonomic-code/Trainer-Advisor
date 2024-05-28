package pro.qyoga.tests.fixture.object_mothers.clients

import pro.azhidkov.platform.spring.sdj.erpo.hydration.AggregateReferenceTarget
import pro.qyoga.app.therapist.clients.cards.EditClientCardForm
import pro.qyoga.core.clients.cards.Client
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.model.ClientRef
import pro.qyoga.core.clients.cards.model.DistributionSource
import pro.qyoga.core.clients.cards.model.DistributionSourceType
import pro.qyoga.core.clients.therapeutic_data.values.TherapeuticDataFieldValue
import pro.qyoga.tests.fixture.data.*
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import java.time.Duration
import java.time.LocalDate
import kotlin.random.Random


object ClientsObjectMother {

    fun createClientCardDtos(count: Int): List<ClientCardDto> =
        (1..count).map { createClientCardDto() }

    fun createEditClientCardForm(
        clientCardDto: ClientCardDto = createClientCardDtoMinimal(),
        customFields: List<TherapeuticDataFieldValue<*>> = emptyList()
    ) = EditClientCardForm(clientCardDto)

    fun createClientCardDto(
        firstName: String = faker.name().firstName(),
        lastName: String = faker.name().lastName(),
        middleName: String? = faker.name().nameWithMiddle().split(" ")[1],
        birthDate: LocalDate = randomBirthDate(),
        phone: String = faker.phoneNumber().phoneNumberInternational(),
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
        distributionSource: DistributionSource? = null,
        version: Long = Random.nextLong(0, Long.MAX_VALUE)
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
        version
    )

    fun createClient(therapistId: Long, clientCardDto: ClientCardDto): Client =
        Client(therapistId, clientCardDto)

    val fakeClientRef: ClientRef = AggregateReferenceTarget(
        createClient(THE_THERAPIST_ID, createClientCardDtoMinimal())
    )

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