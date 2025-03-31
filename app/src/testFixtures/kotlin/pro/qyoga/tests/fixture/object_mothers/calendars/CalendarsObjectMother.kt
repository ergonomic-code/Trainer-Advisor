package pro.qyoga.tests.fixture.object_mothers.calendars

import org.instancio.Instancio
import org.instancio.InstancioApi
import pro.qyoga.core.calendar.ical.model.ICalCalendarItem
import pro.qyoga.core.calendar.ical.model.ICalEventId
import pro.qyoga.tests.fixture.data.asiaNovosibirskTimeZone
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.data.randomElementOf
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother
import pro.qyoga.tests.fixture.object_mothers.appointments.randomAppointmentDate
import pro.qyoga.tests.platform.instancio.KSelect.Companion.field


object CalendarsObjectMother {

    private val aCalendarItemModel = Instancio.of(ICalCalendarItem::class.java)
        .run {
            set(field(ICalCalendarItem::id), aICalEventId())
            set(field(ICalCalendarItem::title), aAppointmentEventTitle())
            set(field(ICalCalendarItem::dateTime), randomAppointmentDate().atZone(asiaNovosibirskTimeZone))
            set(field(ICalCalendarItem::duration), AppointmentsObjectMother.fullCardDuration)
        }
        .withSeed(0L)
        .toModel()

    fun aICalEventId() = ICalEventId(faker.internet().uuid().toString())

    fun aCalendarItem(configureInstance: InstancioApi<ICalCalendarItem>.() -> InstancioApi<ICalCalendarItem> = { this }): ICalCalendarItem {
        return Instancio.of(aCalendarItemModel).run {
            this.configureInstance()
        }
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

