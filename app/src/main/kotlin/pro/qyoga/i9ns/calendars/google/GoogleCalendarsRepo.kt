package pro.qyoga.i9ns.calendars.google

import pro.qyoga.core.users.therapists.TherapistRef


class GoogleCalendarsRepo {

    private val repo = HashMap<TherapistRef, List<GoogleCalendar>>()

    fun addCalendarsToTherapist(therapist: TherapistRef, calendars: List<GoogleCalendar>) {
        repo[therapist] = (repo[therapist] ?: emptyList()) + calendars
    }

    fun getCalendars(therapist: TherapistRef): List<GoogleCalendar> {
        return repo[therapist] ?: emptyList()
    }

}
