package nsu.fit.qyoga.core.questionnaires.internal.repository

import nsu.fit.qyoga.core.questionnaires.api.model.Question
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestionRepo :  CrudRepository<Question, Long> {
    fun findAllByQuestionnaireId(id: Long): List<Question>
}