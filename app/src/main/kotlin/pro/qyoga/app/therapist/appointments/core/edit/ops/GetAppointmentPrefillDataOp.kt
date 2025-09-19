package pro.qyoga.app.therapist.appointments.core.edit.ops

import org.springframework.stereotype.Component
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.app.therapist.appointments.core.edit.forms.CreateAppointmentForm
import pro.qyoga.app.therapist.appointments.core.edit.view_model.SourceItem
import pro.qyoga.app.therapist.appointments.core.edit.view_model.googleEventId
import pro.qyoga.app.therapist.appointments.core.edit.view_model.icsEventId
import pro.qyoga.core.calendar.google.GoogleCalendar
import pro.qyoga.core.calendar.google.GoogleCalendarsService
import pro.qyoga.core.calendar.ical.ICalCalendarsRepo
import pro.qyoga.core.calendar.ical.model.ICalCalendar
import pro.qyoga.core.users.auth.model.UserRef
import pro.qyoga.core.users.settings.UserSettingsRepo
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.LocalDateTime


@Component
class GetAppointmentPrefillDataOp(
    private val iCalCalendarsRepo: ICalCalendarsRepo,
    private val googleCalendarsService: GoogleCalendarsService,
    private val userSettingsRepo: UserSettingsRepo,
    private val timeZones: TimeZones,
) : (TherapistRef, SourceItem?, LocalDateTime?) -> CreateAppointmentForm {

    override fun invoke(
        therapistRef: TherapistRef,
        sourceItem: SourceItem?,
        dateTime: LocalDateTime?
    ): CreateAppointmentForm {
        val currentUserTimeZone = userSettingsRepo.getUserTimeZone(UserRef(therapistRef))

        val sourceEvent = when (sourceItem?.type) {
            ICalCalendar.TYPE ->
                iCalCalendarsRepo.findById(therapistRef, sourceItem.icsEventId())

            GoogleCalendar.TYPE ->
                googleCalendarsService.findById(therapistRef, sourceItem.googleEventId())

            else ->
                null
        }

        val timeZone = sourceEvent?.dateTime?.zone
            ?: currentUserTimeZone
        val timeZoneTitle = timeZones.findById(timeZone)?.displayName

        return CreateAppointmentForm(sourceEvent, dateTime, timeZone, timeZoneTitle)
    }

}