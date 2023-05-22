package nsu.fit.qyoga.core.therapists.internal

import nsu.fit.qyoga.core.therapists.api.model.Therapist
import org.springframework.data.repository.CrudRepository

interface TherapistRepo : CrudRepository<Therapist, Long> {

    fun getTherapistsById(id: Long): Therapist?
}
