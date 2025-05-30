package pro.qyoga.tests.fixture.object_mothers.clients

import org.springframework.data.jdbc.core.mapping.AggregateReference
import pro.azhidkov.platform.spring.sdj.ergo.hydration.AggregateReferenceTarget
import pro.azhidkov.platform.uuid.UUIDv7
import pro.qyoga.app.therapist.clients.list.ClientListItemView
import pro.qyoga.core.clients.cards.Client
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.model.*
import pro.qyoga.core.users.therapists.Therapist
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.data.*
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.util.*


object ClientsObjectMother {

    fun createClientCardDtos(count: Int): List<ClientCardDto> =
        (1..count).map { createClientCardDto() }

    fun createClientCardDto(
        firstName: String = faker.name().firstName(),
        lastName: String = faker.name().lastName(),
        middleName: String? = faker.name().nameWithMiddle().split(" ")[1],
        birthDate: LocalDate = randomBirthDate(),
        phone: String = randomPhoneNumber().toUIFormat(),
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
        phone: String = randomPhoneNumber().toUIFormat(),
        email: String? = null,
        address: String? = null,
        complains: String? = null,
        anamnesis: String? = null,
        distributionSource: DistributionSource? = null,
        version: Long = faker.random().nextLong(0, Long.MAX_VALUE)
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

    fun createClient(therapistId: UUID, clientCardDto: ClientCardDto): Client =
        Client(therapistId, clientCardDto)

    fun aClientMinimal(
        therapistRef: TherapistRef = THE_THERAPIST_REF,
        firstName: String = randomCyrillicWord(),
        lastName: String = randomCyrillicWord(),
        middleName: String? = null,
        birthDate: LocalDate? = null,
        phone: String = randomPhoneNumber().toUIFormat(),
        email: String? = null,
        address: String? = null,
        complains: String? = null,
        anamnesis: String? = null,
        distributionSource: DistributionSource? = null,
        version: Long = faker.random().nextLong(0, Long.MAX_VALUE),
    ) = Client(
        therapistRef.id!!, createClientCardDtoMinimal(
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
            version
        )
    )

    fun randomId() =
        UUIDv7.randomUUID()

    val fakeClientRef: ClientRef = AggregateReferenceTarget(
        createClient(THE_THERAPIST_ID, createClientCardDtoMinimal())
    )

    fun aClientListItemView(
        id: UUID = randomId(),
        firstName: String = faker.name().firstName(),
        lastName: String = faker.name().lastName(),
        middleName: String? = null,
        journalEntriesCount: Int = 0,
        lastEntryDate: LocalDate? = null,
        therapistRef: AggregateReference<Therapist, UUID> = THE_THERAPIST_REF,
        createdAt: Instant = Instant.now()
    ) =
        ClientListItemView(
            id,
            firstName,
            lastName,
            middleName,
            journalEntriesCount,
            lastEntryDate,
            therapistRef,
            createdAt
        )

}

val minBirthDate: LocalDate = LocalDate.ofYearDay(1960, 1)
const val MIN_AGE = 6L
val maxBirthDate: LocalDate = LocalDate.now().minusDays(MIN_AGE * 365)

fun randomBirthDate(): LocalDate =
    randomLocalDate(minBirthDate, Duration.between(minBirthDate.atStartOfDay(), maxBirthDate.atStartOfDay()))

fun randomPhoneNumber() = PhoneNumber.of(
    "+7-${faker.random().nextInt(900, 999)}-${faker.random().nextInt(100, 999)}-${
        faker.random().nextInt(10, 99)
    }-${faker.random().nextInt(10, 99)}"
)

fun randomDistributionSource(): DistributionSource {
    val type = DistributionSourceType.entries.randomElement()
    val hasComment = faker.random().nextBoolean()
    val comment = if (hasComment) randomSentence(1, 5) else null
    return DistributionSource(type, comment)
}