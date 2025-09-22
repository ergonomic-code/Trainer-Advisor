package pro.qyoga.app.therapist.appointments.core.edit.ops

import org.springframework.stereotype.Component
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.app.therapist.appointments.core.edit.forms.CreateAppointmentForm
import pro.qyoga.app.therapist.appointments.core.edit.view_model.SourceItem
import pro.qyoga.app.therapist.appointments.core.edit.view_model.googleEventId
import pro.qyoga.app.therapist.appointments.core.edit.view_model.icsEventId
import pro.qyoga.core.users.auth.model.UserRef
import pro.qyoga.core.users.settings.UserSettingsRepo
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.calendars.google.GoogleCalendar
import pro.qyoga.i9ns.calendars.google.GoogleCalendarsService
import pro.qyoga.i9ns.calendars.ical.ICalCalendarsRepo
import pro.qyoga.i9ns.calendars.ical.model.ICalCalendar
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

        val timeZoneTitle = timeZones.findById(currentUserTimeZone)?.displayName

        return CreateAppointmentForm(sourceEvent, dateTime, currentUserTimeZone, timeZoneTitle)
    }

}
