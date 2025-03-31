package pro.qyoga.app.therapist.appointments.core.edit.ops

import org.springframework.stereotype.Component
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.app.therapist.appointments.core.edit.forms.CreateAppointmentForm
import pro.qyoga.app.therapist.appointments.core.edit.view_model.SourceItem
import pro.qyoga.app.therapist.appointments.core.edit.view_model.icsEventId
import pro.qyoga.core.calendar.ical.ICalCalendarsRepo
import pro.qyoga.core.users.auth.model.UserRef
import pro.qyoga.core.users.settings.UserSettingsRepo
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.LocalDateTime


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

        val timeZone = iCalEvent?.dateTime?.zone
            ?: currentUserTimeZone
        val timeZoneTitle = timeZones.findById(timeZone)?.displayName

        return CreateAppointmentForm(iCalEvent, dateTime, timeZone, timeZoneTitle)
    }

}