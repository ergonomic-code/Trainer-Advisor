package pro.qyoga.i9ns.calendars.google.model

import pro.qyoga.core.users.therapists.TherapistRef

data class GoogleCalendarSettings(
    val ownerRef: TherapistRef,
    val googleAccountRef: GoogleAccountRef,
    val calendarId: GoogleCalendarId,
    val shouldBeShown: Boolean,
)
