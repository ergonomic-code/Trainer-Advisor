package nsu.fit.qyoga.core.questionnaires.internal

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.model.Questionnaire
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = false)
interface QuestionnaireRepo : CrudRepository<Questionnaire, Long>, PagingAndSortingRepository<Questionnaire, Long> {

    fun countAllByTitleContaining(title: String) : Long

    @Query("SELECT COUNT(*) FROM questionnaires")
    fun countAll() : Long

    fun findAllByTitleContaining(title: String, pageable: Pageable): List<QuestionnaireDto>
}