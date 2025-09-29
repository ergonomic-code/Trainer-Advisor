package pro.qyoga.tests.fixture.object_mothers.calendars.google

import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.calendars.google.model.*
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.data.randomElementOf
import pro.qyoga.tests.fixture.object_mothers.appointments.randomAppointmentDuration
import pro.qyoga.tests.fixture.object_mothers.calendars.CalendarsObjectMother.aAppointmentEventTitle
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import java.time.Duration
import java.time.temporal.Temporal
import java.util.*


object GoogleCalendarObjectMother {

    fun aGoogleAccount(
        therapist: TherapistRef,
        email: String = faker.internet().emailAddress()
    ): GoogleAccount =
        GoogleAccount(
            therapist,
            email,
            aGoogleToken()
        )

    fun aGoogleToken(): String {
        val bytes = faker.random().nextRandomBytes(64)
        val core = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
        return "1//$core"
    }

    fun aGoogleCalendar(
        ownerRef: TherapistRef,
        externalId: String,
        name: String = aCalendarName(),
    ) = GoogleCalendar(
        ownerRef,
        externalId,
        name,
    )

    fun <DATE : Temporal> aGoogleCalendarItem(
        calendarId: String = faker.random().randomElementOf(googleCalendarIds),
        date: () -> DATE,
        duration: Duration = randomAppointmentDuration()
    ) =
        GoogleCalendarItem(
            GoogleCalendarItemId(calendarId, faker.internet().uuid()),
            aAppointmentEventTitle(),
            "",
            date(),
            duration,
            null
        )

    fun aCalendarName(): String = faker.random().randomElementOf(
        (1..10).map { faker.internet().emailAddress() } +
                listOf(
                    "Рабочий календарь",
                    "Личные занятия",
                    "Групповые тренировки",
                    "Онлайн сессии",
                    "Терапевтические занятия",
                    "Йога практики",
                    "Индивидуальные консультации",
                    "Семинары и мастер-классы"
                )
    )

    fun aGoogleCalendarSettings(
        googleAccountRef: GoogleAccountRef,
        therapistRef: TherapistRef = THE_THERAPIST_REF,
        externalId: String = faker.internet().uuid(),
        shouldBeShown: Boolean = false
    ): GoogleCalendarSettings =
        GoogleCalendarSettings(
            therapistRef,
            googleAccountRef,
            externalId,
            shouldBeShown
        )

}

val googleCalendarIds = listOf(
    "primary",
    "calendar1@gmail.com",
    "calendar2@gmail.com",
    "work.calendar@company.com",
    "personal.events@gmail.com",
    "yoga.sessions@studio.com",
    "therapy.appointments@clinic.com",
    "group.classes@fitness.com"
)
