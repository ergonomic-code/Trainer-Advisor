package nsu.fit.qyoga.core.completingQuestionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingSearchDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.services.CompletingService
import nsu.fit.qyoga.core.completingQuestionnaires.internal.repository.CompletingJdbcTemplateRepo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CompletingServiceImpl(
    val completingJdbcTemplateRepo: CompletingJdbcTemplateRepo
) : CompletingService {

    override fun findCompletingByTherapistId(
        therapistId: Long,
        completingSearchDto: CompletingSearchDto,
        pageable: Pageable
    ): Page<CompletingDto> {
        return completingJdbcTemplateRepo.findQuestionnaireCompletingById(
            therapistId,
            completingSearchDto,
            pageable
        )
    }
}
