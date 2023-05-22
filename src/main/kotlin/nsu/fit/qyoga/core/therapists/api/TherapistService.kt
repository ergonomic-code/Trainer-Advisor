package nsu.fit.qyoga.core.therapists.api

import nsu.fit.qyoga.core.therapists.api.model.Therapist

interface TherapistService {

    fun findTherapistByUserId(id: Long): Therapist?
}
