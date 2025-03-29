package pro.qyoga.tests.fixture.object_mothers.calendars

import org.instancio.Instancio
import org.instancio.InstancioApi
import org.instancio.InstancioClassApi
import pro.qyoga.core.calendar.ical.model.LocalizedICalCalendarItem
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.data.randomElementOf
import pro.qyoga.tests.fixture.object_mothers.appointments.randomAppointmentDate
import pro.qyoga.tests.fixture.object_mothers.appointments.randomAppointmentDuration
import pro.qyoga.tests.platform.instancio.KSelect.Companion.field


object CalendarsObjectMother {

    fun aLocalizedCalendarItem(configureInstance: InstancioClassApi<LocalizedICalCalendarItem>.() -> InstancioApi<LocalizedICalCalendarItem>): LocalizedICalCalendarItem {
        return Instancio.of(LocalizedICalCalendarItem::class.java)
            .run {
                this.set(field(LocalizedICalCalendarItem::title), aAppointmentEventTitle())
                this.set(field(LocalizedICalCalendarItem::dateTime), randomAppointmentDate())
                this.set(field(LocalizedICalCalendarItem::duration), randomAppointmentDuration())
                this.configureInstance()
            }
            .withSeed(0L)
            .create()
    }

    fun aAppointmentEventTitle() = faker.random().randomElementOf(
        listOf(
            "ИЗ с Юлей",
            "Онлайн практика в канале",
            "Занятие с Ариной Горшковой",
            "Занятие с девочками Яндекс",
            "Онлайн занятие с Еленой Боженовой"
        )
    )

}

