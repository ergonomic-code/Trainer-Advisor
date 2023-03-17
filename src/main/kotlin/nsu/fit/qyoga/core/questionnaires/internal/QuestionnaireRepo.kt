package nsu.fit.qyoga.core.questionnaires.internal

import nsu.fit.qyoga.core.questionnaires.api.dtos.QuestionnaireDto
import nsu.fit.qyoga.core.questionnaires.api.model.Questionnaire
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = false)
interface QuestionnaireRepo : CrudRepository<Questionnaire, Long>, PagingAndSortingRepository<Questionnaire, Long> {

    fun countAllByTitleContaining(title: String): Long
    fun findAllByTitleContaining(title: String, pageable: Pageable): List<Questionnaire>
}
fun QuestionnaireRepo.findPageByTitle(title: String, pageable: Pageable): Page<Questionnaire> {
    val entities = this.findAllByTitleContaining(title, pageable)
    val count = this.countAllByTitleContaining(title)
    return PageImpl(entities.map { Questionnaire(id = it.id, title = it.title) }.toList(), pageable, count)
}
