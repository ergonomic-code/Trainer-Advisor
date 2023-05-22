package nsu.fit.qyoga.core.therapists.internal

import nsu.fit.qyoga.core.therapists.api.TherapistService
import nsu.fit.qyoga.core.therapists.api.model.Therapist
import org.springframework.stereotype.Service

@Service
class TherapistServiceImpl(
    private val therapistRepo: TherapistRepo
) : TherapistService {
    override fun findTherapistByUserId(id: Long): Therapist? {
        return therapistRepo.getTherapistsById(id)
    }
}
