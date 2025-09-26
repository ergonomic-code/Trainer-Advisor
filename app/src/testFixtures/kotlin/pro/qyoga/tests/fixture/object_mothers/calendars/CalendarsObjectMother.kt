package pro.qyoga.tests.fixture.object_mothers.calendars

import org.instancio.Instancio
import org.instancio.InstancioApi
import org.instancio.Model
import pro.qyoga.i9ns.calendars.ical.model.ICalCalendarItem
import pro.qyoga.i9ns.calendars.ical.model.ICalEventId
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
    private val aCalendarItemModel: Model<ICalCalendarItem<ZonedDateTime>> = Instancio.of(ICalCalendarItem::class.java)
        .withTypeParameters(ZonedDateTime::class.java)
        .run {
            generateBy(field(ICalCalendarItem<ZonedDateTime>::id)) { aICalEventId() }
            generateBy(field(ICalCalendarItem<ZonedDateTime>::title)) { aAppointmentEventTitle() }
            generateBy(field(ICalCalendarItem<ZonedDateTime>::dateTime)) {
                randomAppointmentDate().atZone(
                    asiaNovosibirskTimeZone
                )
            }
            generateBy(field(ICalCalendarItem<ZonedDateTime>::duration)) { AppointmentsObjectMother.fullCardDuration }
        }
        .withSeed(faker.random().nextLong())
        .toModel() as Model<ICalCalendarItem<ZonedDateTime>>

    fun aICalEventId() = ICalEventId(faker.internet().uuid().toString())

    fun aCalendarItem(configureInstance: InstancioApi<ICalCalendarItem<ZonedDateTime>>.() -> InstancioApi<ICalCalendarItem<ZonedDateTime>> = { this }): ICalCalendarItem<ZonedDateTime> {
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
