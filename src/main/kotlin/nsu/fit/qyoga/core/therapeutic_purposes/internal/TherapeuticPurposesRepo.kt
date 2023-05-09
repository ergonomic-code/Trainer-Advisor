package nsu.fit.qyoga.core.therapeutic_purposes.internal

import nsu.fit.qyoga.core.therapeutic_purposes.api.TherapeuticPurpose
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
@JvmDefaultWithoutCompatibility
interface TherapeuticPurposesRepo : CrudRepository<TherapeuticPurpose, Long> {

    fun save(therapeuticPurpose: TherapeuticPurpose): TherapeuticPurpose {
        val id = upsert(therapeuticPurpose)
        return therapeuticPurpose.copy(id = id)
    }

    // Почему-то, если делать ON CONFLICT ... DO NOTHING, то ид существующей записи не возвращается
    // На stackoverflow есть страшные запросы, как это провернуть - https://stackoverflow.com/a/42217872,
    // но сделать DO UPDATE явно проще
    @Query(
        """
        INSERT INTO therapeutic_purposes (purpose) VALUES (:#{#therapeuticPurpose.purpose})
        ON CONFLICT ON CONSTRAINT therapeutic_purposes_purpose_key DO UPDATE SET purpose = excluded.purpose
        RETURNING id
        """
    )
    fun upsert(@Param("therapeuticPurpose") therapeuticPurpose: TherapeuticPurpose): Long

}
