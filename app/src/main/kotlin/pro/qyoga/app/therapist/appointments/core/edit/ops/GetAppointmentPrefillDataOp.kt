package pro.qyoga.app.therapist.appointments.core.edit.ops

import net.fortuna.ical4j.model.component.VEvent
import org.springframework.stereotype.Component
import pro.azhidkov.platform.java.time.toLocalDateTime
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.app.therapist.appointments.core.edit.forms.CreateAppointmentForm
import pro.qyoga.app.therapist.appointments.core.edit.view_model.SourceItem
import pro.qyoga.app.therapist.appointments.core.edit.view_model.icsEventId
import pro.qyoga.app.therapist.appointments.core.edit.view_model.toQueryParamStr
import pro.qyoga.core.calendar.ical.ICalCalendarsRepo
import pro.qyoga.core.calendar.ical.platform.ical4j.id
import pro.qyoga.core.users.auth.model.UserRef
import pro.qyoga.core.users.settings.UserSettingsRepo
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.Temporal
import java.time.temporal.TemporalQueries


val VEvent.javaDuration: Duration
    get() = Duration.parse(this.duration.value)

val VEvent.startDateTime: ZonedDateTime
    get() = this.getDateTimeStart<Temporal>().date
        .let {
            it as? ZonedDateTime
                ?: if (it.query(TemporalQueries.zone()) != null) ZonedDateTime.from(it)
                else ZonedDateTime.of(it.toLocalDateTime(ZoneId.systemDefault()), ZoneId.systemDefault())
        }

@Component
class GetAppointmentPrefillDataOp(
    private val iCalCalendarsRepo: ICalCalendarsRepo,
    private val userSettingsRepo: UserSettingsRepo,
    private val timeZones: TimeZones,
) : (TherapistRef, SourceItem?, LocalDateTime?) -> CreateAppointmentForm {

    override fun invoke(
        therapistRef: TherapistRef,
        sourceItem: SourceItem?,
        dateTime: LocalDateTime?
    ): CreateAppointmentForm {
        val currentUserTimeZone = userSettingsRepo.getUserTimeZone(UserRef(therapistRef))
        val iCalEvent = sourceItem?.icsEventId()
            ?.let { iCalCalendarsRepo.findById(therapistRef, it) }
        val timeZone = iCalEvent?.startDateTime?.zone ?: currentUserTimeZone
        return CreateAppointmentForm(
            externalId = iCalEvent?.id?.toQueryParamStr(),
            dateTime = dateTime,
            timeZone = timeZone,
            timeZoneTitle = timeZones.findById(timeZone)?.displayName,
            duration = iCalEvent?.javaDuration,
            comment = iCalEvent?.description?.value,
            place = iCalEvent?.location?.value,
            client = null,
            clientTitle = null,
            appointmentType = null,
            appointmentTypeTitle = null,
            therapeuticTask = null,
            therapeuticTaskTitle = null,
            cost = null,
            payed = null,
            appointmentStatus = null,
        )

    }

}