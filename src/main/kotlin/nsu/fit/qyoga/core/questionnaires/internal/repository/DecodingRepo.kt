package nsu.fit.qyoga.core.questionnaires.internal.repository

import nsu.fit.qyoga.core.questionnaires.api.model.Decoding
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DecodingRepo : CrudRepository<Decoding, Long> {

    fun findAllByQuestionnaireId(id: Long): List<Decoding>
}