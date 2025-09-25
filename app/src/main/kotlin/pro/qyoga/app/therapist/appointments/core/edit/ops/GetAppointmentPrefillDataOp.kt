package pro.qyoga.app.therapist.appointments.core.edit.ops

import org.springframework.stereotype.Component
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.app.therapist.appointments.core.edit.forms.CreateAppointmentForm
import pro.qyoga.core.calendar.api.SourceItem
import pro.qyoga.core.calendar.gateways.CalendarItemsResolver
import pro.qyoga.core.users.auth.model.UserRef
import pro.qyoga.core.users.settings.UserSettingsRepo
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.LocalDateTime


@Component
class GetAppointmentPrefillDataOp(
    private val calendarItemsResolver: CalendarItemsResolver,
    private val userSettingsRepo: UserSettingsRepo,
    private val timeZones: TimeZones,
) : (TherapistRef, SourceItem?, LocalDateTime?) -> CreateAppointmentForm {

    override fun invoke(
        therapistRef: TherapistRef,
        sourceItem: SourceItem?,
        dateTime: LocalDateTime?
    ): CreateAppointmentForm {
        val currentUserTimeZone = userSettingsRepo.getUserTimeZone(UserRef(therapistRef))

        val sourceEvent = calendarItemsResolver.findCalendarItem(therapistRef, sourceItem!!)

        val timeZoneTitle = timeZones.findById(currentUserTimeZone)?.displayName

        return CreateAppointmentForm(sourceEvent, dateTime, currentUserTimeZone, timeZoneTitle)
    }

}
