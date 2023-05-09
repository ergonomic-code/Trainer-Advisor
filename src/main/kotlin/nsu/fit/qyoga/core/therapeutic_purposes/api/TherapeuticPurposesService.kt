package nsu.fit.qyoga.core.therapeutic_purposes.api

interface TherapeuticPurposesService {

    fun save(therapeuticPurpose: TherapeuticPurpose): TherapeuticPurpose

    fun saveAll(chosenPurposes: List<TherapeuticPurpose>): List<TherapeuticPurpose>

}
