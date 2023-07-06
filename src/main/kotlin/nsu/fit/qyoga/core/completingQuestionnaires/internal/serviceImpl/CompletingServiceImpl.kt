package nsu.fit.qyoga.core.completingQuestionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.dtos.CompletingSearchDto
import nsu.fit.qyoga.core.completingQuestionnaires.api.model.Completing
import nsu.fit.qyoga.core.completingQuestionnaires.api.services.CompletingService
import nsu.fit.qyoga.core.completingQuestionnaires.internal.repository.CompletingJdbcTemplateRepo
import nsu.fit.qyoga.core.completingQuestionnaires.internal.repository.CompletingRepo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CompletingServiceImpl(
    val completingJdbcTemplateRepo: CompletingJdbcTemplateRepo,
    val completingRepo: CompletingRepo
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

    override fun saveCompleting(completing: Completing): Long {
        return completingRepo.save(completing).id
    }
}
