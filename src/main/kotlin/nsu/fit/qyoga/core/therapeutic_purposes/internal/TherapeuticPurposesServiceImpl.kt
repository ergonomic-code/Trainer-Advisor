package nsu.fit.qyoga.core.therapeutic_purposes.internal

import nsu.fit.qyoga.core.therapeutic_purposes.api.TherapeuticPurpose
import nsu.fit.qyoga.core.therapeutic_purposes.api.TherapeuticPurposesService
import org.springframework.stereotype.Service

@Service
class TherapeuticPurposesServiceImpl(
    private val therapeuticPurposesRepo: TherapeuticPurposesRepo
) : TherapeuticPurposesService {

    override fun save(therapeuticPurpose: TherapeuticPurpose): TherapeuticPurpose {
        return therapeuticPurposesRepo.save(therapeuticPurpose)
    }

    override fun saveAll(chosenPurposes: List<TherapeuticPurpose>): List<TherapeuticPurpose> {
        return chosenPurposes.map { save(it) }
    }

}
