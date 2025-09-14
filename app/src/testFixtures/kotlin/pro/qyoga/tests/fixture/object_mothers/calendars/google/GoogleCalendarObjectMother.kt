package pro.qyoga.tests.fixture.object_mothers.calendars.google

import pro.qyoga.core.calendar.google.GoogleCalendar
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.data.randomElementOf


object GoogleCalendarObjectMother {

    fun aGoogleCalendar(
        ownerRef: TherapistRef,
        externalId: String,
        name: String = aCalendarName(),
    ) = GoogleCalendar(
        ownerRef,
        externalId,
        name,
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

}