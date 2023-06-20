package nsu.fit.qyoga.core.completingQuestionnaires.internal.repository

import nsu.fit.qyoga.core.completingQuestionnaires.api.model.Completing
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = false)
interface CompletingRepo : CrudRepository<Completing, Long>, PagingAndSortingRepository<Completing, Long>