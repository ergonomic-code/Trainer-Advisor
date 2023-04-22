package nsu.fit.qyoga.core.completingQuestionnaires.api.services

import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingListDto

interface CompletingService {

    fun findCompletingByQId(id: Long): CompletingListDto
}