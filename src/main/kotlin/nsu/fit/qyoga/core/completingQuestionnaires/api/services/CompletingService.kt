package nsu.fit.qyoga.core.completingQuestionnaires.api.services

import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingFindDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CompletingService {

    fun findCompletingByQId(questionnaireId: Long, therapistId: Long, completingFindDto: CompletingFindDto, pageable: Pageable): Page<CompletingDto>
}