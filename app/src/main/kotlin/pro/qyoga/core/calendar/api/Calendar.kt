package pro.qyoga.core.calendar.api

import pro.qyoga.core.users.therapists.TherapistRef


interface Calendar {
    val ownerRef: TherapistRef
    val name: String
    val type: String
}