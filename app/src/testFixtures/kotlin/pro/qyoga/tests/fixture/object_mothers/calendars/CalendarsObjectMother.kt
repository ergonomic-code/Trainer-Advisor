package pro.qyoga.tests.fixture.object_mothers.calendars

import org.instancio.Instancio
import org.instancio.InstancioApi
import org.instancio.Model
import pro.qyoga.i9ns.calendars.ical.model.ICalCalendarItem
import pro.qyoga.i9ns.calendars.ical.model.ICalEventId
import pro.qyoga.i9ns.calendars.ical.model.ICalZonedCalendarItem
import pro.qyoga.tests.fixture.data.asiaNovosibirskTimeZone
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.data.randomElementOf
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother
import pro.qyoga.tests.fixture.object_mothers.appointments.randomAppointmentDate
import pro.qyoga.tests.platform.instancio.KSelect.Companion.field
import pro.qyoga.tests.platform.instancio.generateBy
import java.time.ZonedDateTime


object CalendarsObjectMother {

    @Suppress("UNCHECKED_CAST")
    private val aCalendarItemModel: Model<ICalZonedCalendarItem> = Instancio.of(ICalCalendarItem::class.java)
        .withTypeParameters(ZonedDateTime::class.java)
        .run {
            generateBy(field(ICalZonedCalendarItem::id)) { aICalEventId() }
            generateBy(field(ICalZonedCalendarItem::title)) { aAppointmentEventTitle() }
            generateBy(field(ICalZonedCalendarItem::dateTime)) {
                randomAppointmentDate().atZone(
                    asiaNovosibirskTimeZone
                )
            }
            generateBy(field(ICalZonedCalendarItem::duration)) { AppointmentsObjectMother.fullCardDuration }
        }
        .withSeed(faker.random().nextLong())
        .toModel() as Model<ICalZonedCalendarItem>

    fun aICalEventId() = ICalEventId(faker.internet().uuid().toString())

    fun aCalendarItem(configureInstance: InstancioApi<ICalZonedCalendarItem>.() -> InstancioApi<ICalZonedCalendarItem> = { this }): ICalZonedCalendarItem {
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
